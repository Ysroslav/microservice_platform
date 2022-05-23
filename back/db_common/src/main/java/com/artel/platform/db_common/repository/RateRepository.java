package com.artel.platform.db_common.repository;

import com.artel.platform.db_common.entity.Rate;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface RateRepository {

    Flux<Rate> findAllRateValid();

    Flux<Rate> findAllRates();

    Mono<UUID> save(final Rate rate);

    Mono<Integer> update(final Rate rate);

    Mono<Rate> findRateById(final UUID id);

    Mono<Integer> deleteRateById(final UUID id);
}
