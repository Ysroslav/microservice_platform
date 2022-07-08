package com.artel.platform.service_payment.enums;

public enum PaymentStatus {

    OPEN("OPEN"),
    SUCCEED("SUCCEED"),
    REFUSED("REFUSED");

    private String value;

    PaymentStatus(final String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
