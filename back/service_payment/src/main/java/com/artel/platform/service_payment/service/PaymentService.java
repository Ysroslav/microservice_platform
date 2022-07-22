package com.artel.platform.service_payment.service;

import com.artel.platform.service_payment.dto.PaymentRateInputDTO;
import com.artel.platform.service_payment.dto.platform.PaymentDTO;
import com.artel.platform.service_payment.dto.platform.PaymentOutputDTO;
import com.artel.platform.service_payment.enums.PaymentProcess;
import com.artel.platform.service_payment.enums.PaymentStatus;
import com.artel.platform.service_payment.enums.StatusPayment;
import com.artel.platform.service_payment.grpc.ArtistGrpcClient;
import com.artel.platform.service_payment.mapper.PaymentMapper;
import com.artel.platform.service_payment.model.Payment;
import com.artel.platform.service_payment.property.CommonProperty;
import com.artel.platform.service_payment.repository.MongoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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
                .doOnNext(payment -> {
                    repository.putMapFields(Payment.class, "keyIdempotent",
                            paymentModel.getKeyIdempotent(),
                            Map.of("paymentProcess", PaymentProcess.PAYMENT_POST_ANSWER,
                                    "idPaymentPlatform", payment.id())).subscribe();
                });

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

    @Transactional
    public Mono<Boolean> updateStatusPayment(final PaymentProcess paymentProcess, final String key){
        return repository
                .findAllByField(Payment.class, "keyIdempotent", key)
                .collectList().map(list -> list.iterator().next())
                .flatMap(this::savePaymentWithArtist)
                .flatMap(idPayment -> repository.put(Payment.class, "idPaymentPlatform", idPayment, "paymentProcess", paymentProcess))
                .map(payment -> payment.getPaymentProcess() == paymentProcess)
                .switchIfEmpty(Mono.just(true));
    }

    private Mono<String> savePaymentWithArtist(final Payment payment){
        if (payment.getPaymentStatus() == PaymentStatus.OPEN) {
            return grpcClient
                    .savePaymentWithArtist(payment.getIdPaymentPlatform(),
                            payment.getEmail(),
                            payment.getRateId()
                    )
                    .map(result -> payment.getIdPaymentPlatform());
        }
        return Mono.empty();
    }

    public Mono<StatusPayment> checkStatusPayment(final String email){
        log.info("start method checkStatusPayment");
        return repository.findBySeveralParameterOrderFieldDesc(
                Payment.class, Map.of("email", email), "dateAdd")
                  .collectList().map(list ->
                        list.iterator().next())
                  .flatMap(this::send)
                  .publishOn(Schedulers.boundedElastic())
                  .doOnNext(this::closePayment)
                  .map(PaymentDTO::statusPayment)
                  .switchIfEmpty(Mono.just(StatusPayment.SUCCEEDED));

    }

    private Mono<PaymentDTO> send(final Payment payment){
        if(payment.getPaymentStatus() == PaymentStatus.OPEN) {
            return sendForCheckPayment(payment.getIdPaymentPlatform(), payment.getKeyIdempotent());
        }
        return Mono.empty();
    }

    private void closePayment(final PaymentDTO payment){
        repository.putMapFields(Payment.class, "idPaymentPlatform", payment.id(),
                Map.of("paymentStatus",
                        paymentMapper.mapStatusPaymentToModel(payment.statusPayment()),
                        "paymentProcess",
                        payment.statusPayment() == StatusPayment.SUCCEEDED
                        || payment.statusPayment() == StatusPayment.CANCELED
                                ? PaymentProcess.PAYMENT_CLOSE
                                : PaymentProcess.PAYMENT_WAIT_STATUS
                )
        ).subscribe();
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
