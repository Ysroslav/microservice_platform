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

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

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
                .map(rates -> rates.stream().sorted(Comparator.comparing(RateDTO::prise)).toList())
                .map(this::getListWithPopular)
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

    private List<RateDTO> getListWithPopular(List<RateDTO> rates){
        if (rates.size() < 2) {
            return rates;
        }
        List<RateDTO> result = new LinkedList<>(rates);
        final int index = (int) IntStream.range(0, rates.size())
                                   .takeWhile(i -> !rates.get(i).isPopular()).count();
        final var rate = result.remove(index);
        if (result.size() == 1) {
            result.add(0, rate);
            return result;
        }
        result.add(1, rate);
        return result;
    }
}
