package com.artel.platform.db_artist.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;

@Repository
@RequiredArgsConstructor
public class ArtistRepository {

    private final DatabaseClient databaseClient;

    public Mono<String> addNewArtistByEmail(final String email){
        return databaseClient
                .sql("insert into artist.tbl_artist(email, date_add, date_update) " +
                     "VALUES (:email, :dateAdd, :dateUpdate) ON CONFLICT(email) " +
                     "DO UPDATE SET email = :email " +
                     "RETURNING id")
                .bind("email", email)
                .bind("dateAdd", ZonedDateTime.now())
                .bind("dateUpdate", ZonedDateTime.now())
                .fetch()
                .first()
                .map(r -> r.get("id").toString());
    }
}
