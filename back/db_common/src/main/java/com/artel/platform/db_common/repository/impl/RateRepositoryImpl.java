package com.artel.platform.db_common.repository.impl;

import com.artel.platform.db_common.entity.Rate;
import com.artel.platform.db_common.mapper.RateMapper;
import com.artel.platform.db_common.repository.RateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class RateRepositoryImpl implements RateRepository {

    private final DatabaseClient databaseClient;
    private final RateMapper mapper;

    @Override
    public Flux<Rate> findAllRateValid() {
        return databaseClient
                .sql("select r.id, r.rate_name, r.description, r.rate_prise, r.term, r.is_valid, r.date_add " +
                     "from common.tbl_rate r where r.is_valid = true")
                .map(mapper)
                .all();
    }

    @Override
    public Flux<Rate> findAllRates() {
        return databaseClient
                .sql("select r.id, r.rate_name, r.description, r.rate_prise, r.term, r.is_valid, r.date_add " +
                     "from common.tbl_rate r")
                .map(mapper)
                .all();
    }

    @Override
    @Transactional
    public Mono<UUID> save(final Rate rate){
        log.info("start method save for rate {}", rate);
        return databaseClient.sql("INSERT INTO common.tbl_rate (rate_name, description, rate_prise, term, is_valid, date_add) " +
                           "VALUES (:rate_name, :description, :rate_prise, :term, :is_valid, :date_add)")
                      .filter((statement, executeFunction) -> statement.returnGeneratedValues("id").execute())
                      .bind("rate_name", rate.rateName())
                      .bind("description", rate.description())
                      .bind("rate_prise", rate.prise())
                      .bind("term", rate.termRate())
                      .bind("is_valid", rate.isActive())
                      .bind("date_add", rate.dateAdd())
                      .fetch().first().map(r -> (UUID) r.get("id"));
    }

    @Override
    @Transactional
    public Mono<Integer> update(final Rate rate){
        return databaseClient.sql("UPDATE common.tbl_rate SET rate_name=:rate_name, " +
                                  "description=:description, rate_prise=:rate_prise, " +
                                  "term=:term, is_valid=:is_valid, date_add=:date_add " +
                                  "WHERE id =:idRate")
                             .bind("rate_name", rate.rateName())
                             .bind("description", rate.description())
                             .bind("rate_prise", rate.prise())
                             .bind("term", rate.termRate())
                             .bind("is_valid", rate.isActive())
                             .bind("date_add", rate.dateAdd())
                             .bind("idRate", Objects.requireNonNull(rate.id()))
                             .fetch().rowsUpdated();
    }

    @Override
    public Mono<Rate> findRateById(final UUID id){
        return databaseClient.sql("select r.id, r.rate_name, r.description, r.rate_prise, r.term, r.is_valid, r.date_add " +
                                  "from common.tbl_rate r where r.id = :idRate")
                .bind("idRate", id)
                .map(mapper)
                .first();
    }

    @Override
    public Mono<Integer> deleteRateById(final UUID id){
        return databaseClient.sql("delete from common.tbl_rate r where r.id = :idRate")
                .bind("idRate", id)
                .fetch().rowsUpdated();
    }
}
