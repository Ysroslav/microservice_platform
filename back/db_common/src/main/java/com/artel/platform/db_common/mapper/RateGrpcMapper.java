package com.artel.platform.db_common.mapper;

import com.artel.platform.service_rates.grpc.Rate;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;

@Component
public class RateGrpcMapper {

    private static final int MONEY_CONVERT = 100;

    public Rate rateToRateGrpc(final com.artel.platform.db_common.entity.Rate rateEntity){
        return Rate.newBuilder()
                   .setId(rateEntity.id())
                   .setRateName(rateEntity.rateName())
                   .setDescription(rateEntity.description())
                   .setPrise(convertLongToString(rateEntity.prise()))
                   .setTermRate(rateEntity.termRate())
                   .setIsActive(rateEntity.isActive())
                   .setIsPopular(rateEntity.isPopular())
                   .setDateAdd(rateEntity.dateAdd().toString()).build();
    }

    public com.artel.platform.db_common.entity.Rate rateGrpcToRate(final Rate rateGrpc, final String idRate){
        return new com.artel.platform.db_common.entity.Rate(
                idRate,
                rateGrpc.getRateName(),
                rateGrpc.getDescription(),
                convertStringToLong(rateGrpc.getPrise()),
                rateGrpc.getTermRate(),
                rateGrpc.getIsActive(),
                rateGrpc.getIsPopular(),
                LocalDateTime.parse(rateGrpc.getDateAdd())
        );
    }

    private long convertStringToLong(final String dec){
        final double result = Double.parseDouble(dec);
        return (long) (result * MONEY_CONVERT);
    }

    private String convertLongToString(final long num){
        final int m = (int) (num % MONEY_CONVERT);
        final String mod = m > 9 ? "." + m : ".0" + m;
        return (num / 100) + mod;
    }
}
