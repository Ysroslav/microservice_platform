package com.artel.platform.service_payment.dto.platform;

import com.artel.platform.service_payment.enums.StatusPayment;
import com.fasterxml.jackson.annotation.JsonProperty;

public record PaymentDTO(
        @JsonProperty("id") String id,
        @JsonProperty("status") StatusPayment statusPayment,
        @JsonProperty("paid") boolean paid,
        @JsonProperty("amount") AmountDTO amount,
        @JsonProperty("confirmation") ConfirmationDTO confirmation,
        @JsonProperty("created_at") String createdAt,
        @JsonProperty("description") String description,
        @JsonProperty("metadata") MetadataDTO metadata,
        @JsonProperty("recipient") RecipientDTO recipient,
        @JsonProperty("refundable") boolean refundable,
        @JsonProperty("test") boolean test
) {
}
