package com.artel.platform.db_common.mapper;

import com.artel.platform.db_common.entity.Rate;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiFunction;

@Component
public class RateMapper implements BiFunction<Row, RowMetadata, Rate> {

    @Override
    public Rate apply(Row row, RowMetadata rowMetadata) {
        return new Rate(
                row.get("id", UUID.class),
                row.get("rate_name", String.class),
                row.get("description", String.class),
                Objects.requireNonNull(row.get("rate_prise", Long.class)),
                Objects.requireNonNull(row.get("term", Integer.class)),
                Objects.requireNonNull(row.get("is_valid", Boolean.class)),
                row.get("date_add", LocalDateTime.class)
        );
    }
}
