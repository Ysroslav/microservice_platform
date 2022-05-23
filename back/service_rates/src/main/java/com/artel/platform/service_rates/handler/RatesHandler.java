package com.artel.platform.service_rates.handler;

import com.artel.platform.service_rates.clients.RateClient;
import com.artel.platform.service_rates.mapper.RateMapper;
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

    private final RateClient rateClient;
    private final RateMapper mapper;

    public Mono<ServerResponse> getAllRates(final ServerRequest request) {
        return rateClient.getAllRateFromDataHandler()
                .map(mapper::rateMapToRateDto)
                .collectList()
                .flatMap(rates -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(rates));
    }
}
