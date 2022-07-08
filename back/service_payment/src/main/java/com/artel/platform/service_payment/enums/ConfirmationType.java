package com.artel.platform.service_payment.enums;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ConfirmationType {

    REDIRECT("redirect");

    private final String confirmation;

    ConfirmationType(final String confirmation){
        this.confirmation = confirmation;
    }

    @JsonValue
    public String getConfirmation(){
        return confirmation;
    }
}
