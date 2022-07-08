package com.artel.platform.db_artist.grpc;

import com.artel.platform.db_artist.repository.ArtistRepository;
import com.artel.platform.db_artist.repository.PaymentRepository;
import com.artel.platform.service_payment.grpc.PaymentArtist;
import com.artel.platform.service_payment.grpc.PaymentArtistRequest;
import com.artel.platform.service_payment.grpc.PaymentArtistResponse;
import com.artel.platform.service_payment.grpc.ReactorPaymentArtistServiceGrpc;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.ZonedDateTime;

@GrpcService
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceGrpcImpl extends ReactorPaymentArtistServiceGrpc.PaymentArtistServiceImplBase {

    static final int TERM_MONTH = 12; // TODO request from service rates

    private final ArtistRepository artistRepository;
    private final PaymentRepository paymentRepository;

    record PaymentRecord( String paymentId, String artistId, String rateId){}
    record PaymentTermRecord(PaymentRecord payment, ZonedDateTime dateAdd, ZonedDateTime dateEnd){}

    @Override
    public Mono<PaymentArtistResponse> addPaymentArtis(
            Mono<PaymentArtistRequest> request
    ) {
        log.info("start method addPaymentArtist");
        return request.map(PaymentArtistRequest::getPayment)
                .flatMap(this::getArtistId)
                .flatMap(this::getPaymentWithTerm)
                .flatMap(payment -> paymentRepository
                        .addPayment(
                                payment.payment().artistId,
                                payment.payment().paymentId,
                                payment.payment().rateId,
                                payment.dateAdd(),
                                payment.dateEnd()))
                .map(list -> PaymentArtistResponse.newBuilder().build())
                      .subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<PaymentRecord> getArtistId(PaymentArtist payment){
        return artistRepository.addNewArtistByEmail(payment.getEmail())
                               .map(id -> new PaymentRecord(payment.getPaymentId(), id, payment.getRateId()))
                               .subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<PaymentTermRecord> getPaymentWithTerm(PaymentRecord payment){
        return paymentRepository.findLastPayment(payment.artistId, TERM_MONTH)
                .map(date -> {
                    final var dateEnd = date == null ? ZonedDateTime.now().plusMonths(TERM_MONTH)
                            : date.plusMonths(TERM_MONTH);
                    return new PaymentTermRecord(payment, ZonedDateTime.now(), dateEnd);
                })
                .subscribeOn(Schedulers.boundedElastic());
    }
}


