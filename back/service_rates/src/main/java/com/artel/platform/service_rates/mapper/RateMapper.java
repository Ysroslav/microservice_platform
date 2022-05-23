package com.artel.platform.service_rates.mapper;

import com.artel.platform.service_rates.dto.RateDTO;
import com.artel.platform.service_rates.grpc.Rate;
import com.artel.platform.service_rates.util.ParseUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class RateMapper {

    public RateDTO rateMapToRateDto(final Rate rate) {
            return new RateDTO(
                    rate.getId(),
                    rate.getRateName(),
                    rate.getDescription(),
                    ParseUtil.convertStringToBigDecimal(rate.getPrise()),
                    rate.getTermRate(),
                    rate.getIsActive(),
                    LocalDateTime.parse(rate.getDateAdd())
            );
    }

    public Rate rateDtoMapToRate(final RateDTO rateDto){
        return Rate.newBuilder()
                .setId(rateDto.id())
                .setRateName(rateDto.name())
                .setDescription(rateDto.description())
                .setPrise(rateDto.prise().toString())
                .setTermRate(rateDto.termRate())
                .setIsActive(rateDto.isActive())
                .setDateAdd(rateDto.dateAdd().toString()).build();
    }
}
