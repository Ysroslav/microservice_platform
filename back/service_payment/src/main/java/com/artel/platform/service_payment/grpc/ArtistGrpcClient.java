package com.artel.platform.service_payment.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ArtistGrpcClient {

    @Value("${grpc.db_artist.client.hostname}")
    private String hostname;

    @Value("${grpc.db_artist.client.port}")
    private Integer port;

    private ManagedChannel channel;
    private ReactorPaymentArtistServiceGrpc.ReactorPaymentArtistServiceStub blockingStub;

    @PostConstruct
    public void defineGRPC() {
        final var channelBuilder = ManagedChannelBuilder
                .forAddress(hostname, port).usePlaintext();
        channel = channelBuilder.build();
        blockingStub = ReactorPaymentArtistServiceGrpc.newReactorStub(channel);
    }

    @PreDestroy
    public void destroyGRPC() {
        try {
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("get repository method shutdown not right: " + e.getMessage());
        }
    }

    public Mono<PaymentArtistResponse> savePaymentWithArtist(
            final String paymentId, final String email, final String rateId){
        final var payment = PaymentArtist.newBuilder()
                .setPaymentId(paymentId).setEmail(email).setRateId(rateId).build();
        return blockingStub.addPaymentArtis(
                PaymentArtistRequest.newBuilder().setPayment(payment).build());
    }


}
