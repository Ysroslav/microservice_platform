package com.artel.platform.service_payment.handler;

import com.artel.platform.service_payment.dto.PaymentRateInputDTO;
import com.artel.platform.service_payment.enums.PaymentProcess;
import com.artel.platform.service_payment.service.PaymentService;
import com.artel.platform.service_payment.validation.PaymentRateInputValidation;
import com.artel.platform.starter_util.exceptions.IllegalParamInRequestException;
import com.artel.platform.starter_util.handler.HandlerErrorForWeb;
import com.artel.platform.starter_util.handler.WebfluxHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class PaymentHandler {

    private final PaymentService paymentService;
    private final HandlerErrorForWeb handlerError;
    private final WebfluxHandler webfluxHandler;


    public Mono<ServerResponse> createPayment(ServerRequest request) {
        return webfluxHandler.handleRequestReactive(
                request
                .bodyToMono(PaymentRateInputDTO.class)
                .doOnNext(payment -> HandlerErrorForWeb.validateObject(payment, new PaymentRateInputValidation()))
                .flatMap(paymentService::createPayment)
                             )
                .flatMap(a -> ServerResponse.status(HttpStatus.CREATED).build())
                .onErrorResume(e -> handlerError.getAttributesError(e, request));
    }

    public Mono<ServerResponse> redirectForPayment(ServerRequest request) {
        return null;
    }

    public Mono<ServerResponse> updatePayment(ServerRequest request) {
        final var clientId = request.queryParam("client_id")
                .orElseThrow(() -> new IllegalParamInRequestException(
                        "key_idempotent not found", PaymentHandler.class, "updatePayment"));

        return webfluxHandler.handleRequestReactive(
                paymentService.updateStatusPayment(PaymentProcess.PAYMENT_REDIRECT_APP, clientId)
                             )
                .flatMap(result -> ServerResponse.status(HttpStatus.CREATED).build())
                .onErrorResume(e -> handlerError.getAttributesError(e, request));
    }

    public Mono<ServerResponse> checkPayment(ServerRequest request) {
        final var clientId = request.queryParam("client_id")
                                         .orElseThrow(() -> new IllegalParamInRequestException(
                                                 "header client_id not found", PaymentHandler.class, "checkPayment"));
        return webfluxHandler.handleRequestReactive(
                paymentService.checkStatusPayment(clientId)
                             )
                             .flatMap(a -> ServerResponse.ok().bodyValue(a.getValue()))
                             .onErrorResume(e -> handlerError.getAttributesError(e, request));
    }
}
