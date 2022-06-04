package com.artel.platform.db_common.entity;

import javax.annotation.Nullable;
import java.time.LocalDateTime;

public record Rate(
        @Nullable String id,
        String rateName,
        String description,
        long prise,
        int termRate,
        boolean isActive,
        boolean isPopular,
        LocalDateTime dateAdd
) {
}
