package com.artel.platform.service_rates.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatesHandler {


    public Mono<ServerResponse> getDataRates(final ServerRequest request) {
        final var rates = Flux.fromStream(Stream.of("common", "ultimate", "vip"));
        return ServerResponse
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(rates, String.class)
                .subscribeOn(Schedulers.boundedElastic());
    }
}
