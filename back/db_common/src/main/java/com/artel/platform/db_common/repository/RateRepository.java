package com.artel.platform.db_common.repository;

import com.artel.platform.db_common.entity.Rate;
import org.springframework.data.relational.core.sql.In;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface RateRepository {

    Flux<Rate> findAllRateValid();

    Flux<Rate> findAllRates();

    Mono<String> save(final Rate rate);

    Mono<Integer> update(final Rate rate);

    Mono<Rate> findRateById(final String id);

    Mono<Integer> deleteRateById(final String id);
}
