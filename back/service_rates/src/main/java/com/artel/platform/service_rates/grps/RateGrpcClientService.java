package com.artel.platform.service_rates.grps;

import com.artel.platform.service_rates.grpc.Rate;
import com.artel.platform.service_rates.grpc.RateByIdRequest;
import com.artel.platform.service_rates.grpc.RateByIdResponse;
import com.artel.platform.service_rates.grpc.RateDeleteRequest;
import com.artel.platform.service_rates.grpc.RateDeleteResponse;
import com.artel.platform.service_rates.grpc.RateSaveRequest;
import com.artel.platform.service_rates.grpc.RateSaveResponse;
import com.artel.platform.service_rates.grpc.RateUpdateRequest;
import com.artel.platform.service_rates.grpc.RatesRequest;
import com.artel.platform.service_rates.grpc.RatesResponse;
import com.artel.platform.service_rates.grpc.ReactorRateServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RateGrpcClientService {

    @Value("${grpc.client.hostname}")
    private String hostname;

    @Value("${grpc.client.port}")
    private Integer port;

    private ManagedChannel channel;
    private ReactorRateServiceGrpc.ReactorRateServiceStub blockingStub;

    @PostConstruct
    public void defineGRPC() {
        final var channelBuilder = ManagedChannelBuilder
                .forAddress(hostname, port).usePlaintext();
        channel = channelBuilder.build();
        blockingStub = ReactorRateServiceGrpc.newReactorStub(channel);
    }

    @PreDestroy
    public void destroyGRPC() {
        try {
            channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            log.error("get repository method shutdown not right: " + e.getMessage());
        }
    }

    public Flux<Rate> getAllRates(){
        final var response = blockingStub.getAllRates(
                RatesRequest.newBuilder().build());
        return response
                .map(
                        RatesResponse::getRateList
                ).flatMapMany(Flux::fromIterable);
    }

    public Mono<Rate> getRateById(final String id){
        final var response = blockingStub.getRateById(
                RateByIdRequest.newBuilder().setIdRate(id).build());
        return response.map(RateByIdResponse::getRate);
    }

    public Mono<Void> saveRate(final Rate rate) {
        final var response = blockingStub.saveRate(
                RateSaveRequest.newBuilder().setRate(rate).build());
        return response.flatMap(res -> Mono.empty());
    }

    public Mono<Void> deleteRate(final String id) {
        final var response = blockingStub.deleteRate(
                RateDeleteRequest.newBuilder().setIdRate(id).build());
        return response.flatMap(res -> Mono.empty());
    }

    public Mono<Void> updateRate(final Rate rate) {
        final var response = blockingStub.updateRateById(
                RateUpdateRequest.newBuilder().setRate(rate).build());
        return response.flatMap(res -> Mono.empty());
    }

}
