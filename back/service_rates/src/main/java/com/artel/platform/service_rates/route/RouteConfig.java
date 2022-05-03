package com.artel.platform.service_rates.route;

import com.artel.platform.service_rates.handler.RatesHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouteConfig {

    @Bean
    public RouterFunction<ServerResponse> route(final RatesHandler handler) {
        return RouterFunctions
                .route(RequestPredicates.GET("/rates/all").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::getDataRates);
    }
}
