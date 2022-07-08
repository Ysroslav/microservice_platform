package com.artel.platform.service_payment.dto.platform;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PaymentOutputDTO(
        @JsonProperty("amount") AmountDTO amount,
        @JsonProperty("capture") boolean capture,
        @JsonProperty("confirmation") ConfirmationInputDTO confirmation,
        @JsonProperty("description") String description
) {
}
