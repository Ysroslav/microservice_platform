package com.artel.platform.service_payment.service;

import com.artel.platform.service_payment.dto.PaymentRateInputDTO;
import com.artel.platform.service_payment.dto.platform.PaymentDTO;
import com.artel.platform.service_payment.dto.platform.PaymentOutputDTO;
import com.artel.platform.service_payment.enums.PaymentProcess;
import com.artel.platform.service_payment.enums.PaymentStatus;
import com.artel.platform.service_payment.grpc.ArtistGrpcClient;
import com.artel.platform.service_payment.mapper.PaymentMapper;
import com.artel.platform.service_payment.model.Payment;
import com.artel.platform.service_payment.property.CommonProperty;
import com.artel.platform.service_payment.repository.MongoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.netty.http.client.HttpClient;

import java.net.URI;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentMapper paymentMapper;
    private final MongoRepository repository;
    private final WebClient webClient;
    private final CommonProperty commonProperty;
    private final ArtistGrpcClient grpcClient;

    public Mono<PaymentDTO> createPayment(final PaymentRateInputDTO inputDTO){
        final var paymentModel = paymentMapper
                .mapPaymentRateInputToPayment(inputDTO, PaymentProcess.PAYMENT_FRONT, PaymentStatus.OPEN);
        return repository.saveIfExist(paymentModel,
                Map.of("keyIdempotent", paymentModel.getKeyIdempotent(),
                        "email", paymentModel.getEmail()),
                        Map.of("paymentProcess", PaymentProcess.PAYMENT_FRONT))
                .flatMap(payment ->
                        sendPaymentToPlatform(paymentMapper.mapPaymentToPaymentOutputDto(payment), payment.getKeyIdempotent()))
                         .publishOn(Schedulers.boundedElastic())
                .map(payment -> {
                    repository.putMapFields(Payment.class, "keyIdempotent",
                            paymentModel.getKeyIdempotent(),
                            Map.of("paymentProcess", PaymentProcess.PAYMENT_POST_ANSWER,
                                    "idPaymentPlatform", payment.id())).subscribe();
                    return payment;
                })
                .doOnNext(payment -> redirectPayment(payment.confirmation().confirmationUrl(), paymentModel));

    }

    private Mono<PaymentDTO> sendPaymentToPlatform(final PaymentOutputDTO paymentOutputDTO, final String idempotence){
        log.info("PaymentOutputDTO {}", paymentOutputDTO);
        return webClient.post()
                 .uri(commonProperty.getPaymentUrl())
                 .headers(httpHeaders -> {
                    httpHeaders.add(commonProperty.getIdempotence(), idempotence);
                    httpHeaders.setBasicAuth(commonProperty.getClient(), commonProperty.getSecret());
                })
                 .body(Mono.just(paymentOutputDTO), PaymentOutputDTO.class)
                 .accept(MediaType.APPLICATION_JSON)
                 .retrieve()
                 .bodyToMono(PaymentDTO.class);
    }

    private void redirectPayment(final String url, final Payment paymentModel){
        log.info("start redirect url {}", url);
        WebClient.builder()
                 .clientConnector(
                    new ReactorClientHttpConnector(
                            HttpClient.create().followRedirect(true)
                    )
        ).build().get()
                 .uri(URI.create(url))
                 .headers(httpHeaders -> {
                     httpHeaders.setLocation(URI.create(url));
                     httpHeaders.setBasicAuth(commonProperty.getClient(), commonProperty.getSecret());
                 })
                 .exchangeToMono(clientResponse -> ServerResponse.status(HttpStatus.SEE_OTHER).build())
                 .publishOn(Schedulers.boundedElastic())
                 .doOnNext(result -> repository.put(Payment.class, "keyIdempotent",
                         paymentModel.getKeyIdempotent(), "paymentProcess", PaymentProcess.PAYMENT_REDIRECT).subscribe())
                 .subscribe();
    }

    @Transactional
    public Mono<Boolean> updateStatusPayment(final PaymentProcess paymentProcess, final String clientId){
        return repository
                .findBySeveralParameterOrderFieldDesc(Payment.class, Map.of("email", clientId, "paymentProcess", PaymentProcess.PAYMENT_REDIRECT), "dateAdd")
                .collectList().map(list -> list.iterator().next())
                .flatMap(payment ->
                        grpcClient.savePaymentWithArtist(payment.getIdPaymentPlatform(), payment.getEmail(), payment.getRateId())
                                .map(result -> payment.getIdPaymentPlatform()))
                .flatMap(idPayment -> repository.put(Payment.class, "idPaymentPlatform", idPayment, "paymentProcess", paymentProcess))
                .map(payment -> payment.getPaymentProcess() == paymentProcess);
    }

    public Mono<PaymentStatus> checkStatusPayment(final String email){
        return repository.findBySeveralParameterOrderFieldDesc(
                Payment.class, Map.of("email", email, "paymentProcess", PaymentProcess.PAYMENT_REDIRECT_APP), "dateAdd")
                  .collectList().map(list -> list.iterator().next())
                  .flatMap(payment -> sendForCheckPayment(payment.getIdPaymentPlatform(), payment.getKeyIdempotent()))
                  .flatMap(payment -> repository.putMapFields(Payment.class, "idPaymentPlatform", payment.id(),
                   Map.of("paymentStatus", paymentMapper.mapStatusPaymentToModel(payment.statusPayment()),
                           "paymentProcess", PaymentProcess.PAYMENT_CLOSE)))
                  .map(Payment::getPaymentStatus);

    }

    private Mono<PaymentDTO> sendForCheckPayment(final String payment_id, final String idempotence){
        return webClient.get()
                        .uri(commonProperty.getCheckPaymentUrl() + payment_id)
                        .headers(httpHeaders -> {
                            httpHeaders.add(commonProperty.getIdempotence(), idempotence);
                            httpHeaders.setBasicAuth(commonProperty.getClient(), commonProperty.getSecret());
                        })
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .bodyToMono(PaymentDTO.class);
    }


}
