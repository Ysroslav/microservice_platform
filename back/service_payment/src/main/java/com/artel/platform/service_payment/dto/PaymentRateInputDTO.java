package com.artel.platform.service_payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PaymentRateInputDTO(
        @JsonProperty("id_device") String idDevice,
        @JsonProperty("key_idempotent") String keyIdempotent,
        @JsonProperty("name_user") String nameUser,
        @JsonProperty("email") String email,
        @JsonProperty("rate_id") String rateId,
        @JsonProperty("amount") String amount,
        @JsonProperty("currency") String currency,
        @JsonProperty("description") String description,
        @JsonProperty("name_rate") String nameRate
) {
}
