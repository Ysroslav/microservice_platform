package com.artel.platform.service_rates.router;

import com.artel.platform.service_rates.handler.RatesAdminHandler;
import com.artel.platform.service_rates.handler.RatesHandler;
import com.artel.platform.service_rates.properties.CommonProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class RouteConfig {

    private final CommonProperty property;
    private final RoleUserFilter filter;

    @Bean
    public RouterFunction<ServerResponse> route(final RatesHandler handler) {
        return RouterFunctions
                .route(RequestPredicates.GET(property.getBasePathCommon() + "/v1/rates")
                                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::getAllRates);
    }

    @Bean
    public RouterFunction<ServerResponse> routerFunctionAdmin(final RatesAdminHandler handler) {
        final var path = property.getBasePathAdmin();
        return RouterFunctions.route()
                .GET(path + "/v1/rate", handler::getRateById)
                .POST(path + "/v1/rate", handler::saveRate)
                .PUT(path + "/v1/rate", handler::updateRate)
                .DELETE(path + "/v1/rate", handler::deleteRateById)
                //.filter(filter) TODO filter for check role admin
                .build();
    }


}
