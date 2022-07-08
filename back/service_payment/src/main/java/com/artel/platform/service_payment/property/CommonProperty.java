package com.artel.platform.service_payment.property;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class CommonProperty {

    @Value("${path.front}")
    private String pathFront;

    @Value("${config.fieldIndex}")
    private String fieldIndex;

    @Value("${payment.path}")
    private String pathPlatform;

    @Value("${payment.timeout}")
    private Integer platformTimeout;

    @Value("${payment.return_url}")
    private String returnUrl;

    @Value("${payment.payment_url}")
    private String paymentUrl;

    @Value("${payment.idempotence}")
    private String idempotence;

    @Value("${payment.client}")
    private String client;

    @Value("${payment.secret}")
    private String secret;

    @Value("${payment.redirect_url}")
    private String redirectUrl;

    @Value("${payment.check_payment_url}")
    private String checkPaymentUrl;
}
