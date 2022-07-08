package com.artel.platform.service_payment.dto.platform;

import com.artel.platform.service_payment.enums.ConfirmationType;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ConfirmationInputDTO(
        @JsonProperty("type") ConfirmationType confirmation,
        @JsonProperty("return_url") String returnUrl
) {

    public ConfirmationType getType(){
        return confirmation;
    }

    public String getReturn_url(){
        return returnUrl;
    }
}
