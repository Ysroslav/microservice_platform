package com.artel.platform.service_rates.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RateDTO(
        String id,
        String name,
        String description,
        BigDecimal prise,
        int termRate,
        boolean isActive,
        LocalDateTime dateAdd
) {
}
