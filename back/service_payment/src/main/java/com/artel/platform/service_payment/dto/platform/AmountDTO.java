package com.artel.platform.service_payment.dto.platform;

import com.artel.platform.service_payment.enums.Currency;
import com.fasterxml.jackson.annotation.JsonProperty;

public record AmountDTO(
    @JsonProperty("value") String value,
    @JsonProperty("currency") Currency currency
) {
}
