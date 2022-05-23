package com.artel.platform.db_common.entity;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record Rate(
        @Nullable UUID id,
        String rateName,
        String description,
        long prise,
        int termRate,
        boolean isActive,
        LocalDateTime dateAdd
) {
}
