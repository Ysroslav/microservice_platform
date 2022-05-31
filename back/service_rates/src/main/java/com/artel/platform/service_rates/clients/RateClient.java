package com.artel.platform.service_rates.clients;

import com.artel.platform.service_rates.grpc.Rate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface RateClient {

    Flux<? extends Rate> getAllRateFromDataHandler();

    Mono<? extends Rate> getRateById(final UUID id);

    Mono<Integer> updateRate(final Rate rate);

    Mono<String> saveRate(final Rate rate);

    Mono<Integer> deleteRateById(final String id);
}
