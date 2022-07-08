package com.artel.platform.service_payment.dto.platform;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RecipientDTO(
        @JsonProperty("account_id") String accountId,
        @JsonProperty("gateway_id") String gatewayId
) {
}
