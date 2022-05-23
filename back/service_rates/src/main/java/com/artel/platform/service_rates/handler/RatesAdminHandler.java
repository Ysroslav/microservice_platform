package com.artel.platform.service_rates.handler;

import com.artel.platform.service_rates.clients.RateClient;
import com.artel.platform.service_rates.dto.RateDTO;
import com.artel.platform.service_rates.exceptions.IllegalRateIdException;
import com.artel.platform.service_rates.mapper.RateMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatesAdminHandler {

    private final RateClient rateClient;
    private final RateMapper mapper;

    public Mono<ServerResponse> getRateById(final ServerRequest request) {
        final var idRate = request.queryParam("rate_id")
                                  .orElseThrow(() -> new IllegalRateIdException("Rate in param is null"));
        return rateClient.getRateById(UUID.fromString(idRate))
                .map(mapper::rateMapToRateDto)
                .flatMap(rate -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(rate))
                .onErrorResume(err -> Mono.error(new RuntimeException("getRateById error")));
    }

    public Mono<ServerResponse> saveRate(ServerRequest request) {
        return request.bodyToMono(RateDTO.class)
                      .map(mapper::rateDtoMapToRate)
                      .flatMap(rateClient::saveRate)
                      .flatMap(rate -> ServerResponse
                                        .status(HttpStatus.CREATED)
                                        .bodyValue("CREATED"));
    }

    public Mono<ServerResponse> updateRate(ServerRequest request) {
        return request.bodyToMono(RateDTO.class)
                      .map(mapper::rateDtoMapToRate)
                      .flatMap(rateClient::updateRate)
                      .flatMap(rate -> ServerResponse
                                            .status(HttpStatus.NO_CONTENT)
                                            .bodyValue("UPDATED"));
    }

    public Mono<ServerResponse> deleteRateById(ServerRequest request) {
        final var idRate = request.queryParam("rate_id")
                                  .orElseThrow(() -> new IllegalRateIdException("Rate in param is null"));
        return rateClient.deleteRateById(UUID.fromString(idRate))
                .flatMap(rate -> ServerResponse.status(HttpStatus.NO_CONTENT).bodyValue("No content"));
    }
}
