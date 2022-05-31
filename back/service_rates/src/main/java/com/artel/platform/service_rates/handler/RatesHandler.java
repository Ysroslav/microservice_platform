package com.artel.platform.service_rates.handler;

import com.artel.platform.service_rates.clients.RateClient;
import com.artel.platform.service_rates.dto.RateDTO;
import com.artel.platform.service_rates.mapper.RateMapper;
import com.artel.platform.starter_util.exceptions.MethodHandlerException;
import com.artel.platform.starter_util.exceptions.NotFoundObjectException;
import com.artel.platform.starter_util.handler.HandlerErrorForWeb;
import com.artel.platform.starter_util.handler.WebfluxHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RatesHandler {

    private final RateClient rateClient;
    private final RateMapper mapper;
    private final HandlerErrorForWeb handlerError;
    private final WebfluxHandler webfluxHandler;

    public Mono<ServerResponse> getAllRates(final ServerRequest request) {
        return webfluxHandler.handleRequestReactive(rateClient.getAllRateFromDataHandler()
                .map(mapper::rateMapToRateDto))
                .collectList()
                .flatMap(rates -> getResult(rates, request))
                .onErrorResume(e ->  handlerError
                        .getAttributesError(new MethodHandlerException(e.getMessage(), RatesHandler.class, "getAllRates"), request));
    }

    private Mono<ServerResponse> getResult(final List<RateDTO> rates, ServerRequest request) {
        if (rates.isEmpty()) {
            return handlerError.getAttributesError(new NotFoundObjectException("getAllRates not found", RatesHandler.class), request);
        }
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(rates);
    }
}
