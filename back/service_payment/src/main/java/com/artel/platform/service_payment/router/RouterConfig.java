package com.artel.platform.service_payment.router;

import com.artel.platform.service_payment.handler.PaymentHandler;
import com.artel.platform.service_payment.property.CommonProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class RouterConfig {

    private final CommonProperty commonProperty;
    private final PaymentHandler handler;

    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions.route()
                              .POST(commonProperty.getPathFront() + "/create", handler::createPayment)
                              .GET(commonProperty.getPathFront() + "/redirect", handler::redirectForPayment) //уточнить
                              .PUT(commonProperty.getPathFront() + "/update/payment", handler::updatePayment)
                              .GET(commonProperty.getPathFront() + "/check/payment", handler::checkPayment)
                              .build();
    }
}
