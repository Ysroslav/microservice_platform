package com.artel.platform.service_payment.model;

import com.artel.platform.service_payment.enums.Currency;
import com.artel.platform.service_payment.enums.PaymentProcess;
import com.artel.platform.service_payment.enums.PaymentStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
public class Payment {

    private String idPaymentPlatform;
    private String idDevice;
    private String keyIdempotent;
    private String nameUser;
    private String email;
    private String rateId;
    private int amount;
    private Currency currency;
    private String description;
    private PaymentStatus paymentStatus;
    private PaymentProcess paymentProcess;
    private LocalDateTime dateAdd;
    private LocalDateTime dateUpdate;
}
