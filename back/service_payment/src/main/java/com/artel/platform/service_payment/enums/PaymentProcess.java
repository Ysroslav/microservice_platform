package com.artel.platform.service_payment.enums;

public enum PaymentProcess {

    PAYMENT_FRONT("PAYMENT_FRONT"),
    PAYMENT_POST("PAYMENT_POST"),
    PAYMENT_POST_ANSWER("PAYMENT_POST_ANSWER"),
    PAYMENT_REDIRECT("PAYMENT_REDIRECT"),
    PAYMENT_REDIRECT_APP("PAYMENT_REDIRECT_APP"),
    PAYMENT_WAIT_STATUS("PAYMENT_WAIT_STATUS"),
    PAYMENT_ANSWER_PLATFORM("PAYMENT_ANSWER_PLATFORM"),
    PAYMENT_CLOSE("PAYMENT_CLOSE");

    private String value;

    PaymentProcess(final String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }

}
