package com.artel.platform.service_rates.clients.impl;

import com.artel.platform.service_rates.clients.RateClient;
import com.artel.platform.service_rates.grpc.Rate;
import com.artel.platform.service_rates.grps.RateGrpcClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RateClientReactorGrpc implements RateClient {

    private final RateGrpcClientService rateClient;

    @Override
    public Flux<? extends Rate> getAllRateFromDataHandler(){
        return rateClient.getAllRates();
    }

    @Override
    public Mono<? extends Rate> getRateById(final UUID id) {
        return rateClient.getRateById(id.toString());
    }

    @Override
    public Mono<Void> updateRate(final Rate rate) {
        return rateClient.updateRate(rate);
    }

    @Override
    public Mono<Void> saveRate(final Rate rate) {
        return rateClient.saveRate(rate);
    }

    @Override
    public Mono<Void> deleteRateById(final UUID id) {
        return rateClient.deleteRate(id.toString());
    }


}
