package com.artel.platform.service_rates.handler;

import com.artel.platform.service_rates.clients.RateClient;
import com.artel.platform.service_rates.dto.RateDTO;
import com.artel.platform.service_rates.exceptions.IllegalRateIdException;
import com.artel.platform.service_rates.mapper.RateMapper;
import com.artel.platform.starter_util.exceptions.IllegalParamInRequestException;
import com.artel.platform.starter_util.exceptions.MethodHandlerException;
import com.artel.platform.starter_util.exceptions.NotFoundObjectException;
import com.artel.platform.starter_util.handler.HandlerErrorForWeb;
import com.artel.platform.starter_util.handler.WebfluxHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatesAdminHandler {

    private final RateClient rateClient;
    private final RateMapper mapper;
    private final HandlerErrorForWeb handlerError;
    private final WebfluxHandler webfluxHandler;

    public Mono<ServerResponse> getRateById(final ServerRequest request) {
        final var idRate = request.queryParam("rate_id")
                                  .orElseThrow(() -> new IllegalParamInRequestException("Rate in param is null", RatesAdminHandler.class, "getRateById"));
        return webfluxHandler.handleRequestReactive(rateClient.getRateById(UUID.fromString(idRate))
                .map(mapper::rateMapToRateDto)
                .flatMap(rate -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(rate))
                .onErrorResume(err ->
                        handlerError.getAttributesError(err, request)));
    }

    public Mono<ServerResponse> saveRate(ServerRequest request) {
        return webfluxHandler.handleRequestReactive(
                request.bodyToMono(RateDTO.class)
                      .map(mapper::rateDtoMapToRate)
                      .flatMap(rateClient::saveRate))
                      .flatMap(rate -> ServerResponse
                                        .status(HttpStatus.CREATED)
                                        .bodyValue(rate))
                      .onErrorResume(err ->
                              handlerError.getAttributesError(new MethodHandlerException(err.getMessage(), RatesAdminHandler.class, "saveRate"), request));
    }

    public Mono<ServerResponse> updateRate(ServerRequest request) {
        return webfluxHandler.handleRequestReactive(
                request.bodyToMono(RateDTO.class)
                      .map(mapper::rateDtoMapToRate)
                      .flatMap(rateClient::updateRate))
                      .flatMap(result -> checkResult(result,"updateRate", request))
                      .onErrorResume(err ->
                              handlerError.getAttributesError(new MethodHandlerException(err.getMessage(), RatesAdminHandler.class, "updateRate"), request));
    }

    public Mono<ServerResponse> deleteRateById(ServerRequest request) {
        final var idRate = request.queryParam("rate_id")
                                  .orElseThrow(() -> new IllegalRateIdException("Rate in param is null"));
        return webfluxHandler.handleRequestReactive(
                rateClient.deleteRateById(idRate))
                .flatMap(result -> checkResult(result,"deleteRateById", request))
                .onErrorResume(err ->
                      handlerError.getAttributesError(new MethodHandlerException(err.getMessage(), RatesAdminHandler.class, "deleteRateById"), request));
    }

    private Mono<ServerResponse> checkResult(final Integer result, String methodName, ServerRequest request) {
        if (result == 0) {
            return handlerError.getAttributesError(new NotFoundObjectException(methodName + " not found", RatesAdminHandler.class), request);
        }
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(result);
    }

    public Mono<ServerResponse> up(ServerRequest request) {
        return ServerResponse.ok().bodyValue("UP");
    }
}
