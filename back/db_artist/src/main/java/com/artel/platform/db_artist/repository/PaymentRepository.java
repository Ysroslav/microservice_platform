package com.artel.platform.db_artist.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PaymentRepository {

    private final DatabaseClient databaseClient;

    public Mono<String> addPayment(final String artistId,
                                   final String paymentId,
                                   final String rateId,
                                   final ZonedDateTime dateAdd,
                                   final ZonedDateTime dateEnd){
        return databaseClient
                .sql("insert into artist.tbl_payment(artist_id, payment_id, rate_id, date_payment, date_end) " +
                     "VALUES(:artistId, :paymentId, :rateId, :datePayment, :dateEnd)")
                .filter((statement, executeFunction) -> statement.returnGeneratedValues("id").execute())
                .bind("artistId", artistId)
                .bind("paymentId", paymentId)
                .bind("rateId", rateId)
                .bind("datePayment", dateAdd)
                .bind("dateEnd", dateEnd)
                .fetch().first().map(r -> r.get("id").toString());
    }

    public Mono<ZonedDateTime> findLastPayment(final String artistId, final int term) {
        return databaseClient
                .sql("SELECT date_end FROM artist.tbl_payment WHERE artist_id = :artistId AND " +
                     "(EXTRACT(YEAR FROM age(:dateStart, date_end)) * 12 + " +
                     "EXTRACT(MONTH FROM age(:dateStart, date_end))) < :term " +
                     "order by date_end desc limit 1")
                .bind("artistId", artistId)
                .bind("term", term)
                .bind("dateStart", ZonedDateTime.now())
                .fetch()
                .first()
                .switchIfEmpty(Mono.just(Map.of("date_end", OffsetDateTime.now())))
                .map(r -> {
                    log.info("test");
                    return ((OffsetDateTime) r.get("date_end")).toZonedDateTime();
                });
    }
}
