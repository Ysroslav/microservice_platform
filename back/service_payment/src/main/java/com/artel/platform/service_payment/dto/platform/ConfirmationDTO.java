package com.artel.platform.service_payment.dto.platform;

import com.artel.platform.service_payment.enums.ConfirmationType;
import com.fasterxml.jackson.annotation.JsonProperty;

public record ConfirmationDTO(
        @JsonProperty("type") ConfirmationType confirmation,
        @JsonProperty("confirmation_url") String confirmationUrl
) {
}
