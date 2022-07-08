package com.artel.platform.service_payment.enums;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum Currency {

    USD("USD"),
    EUR("EUR"),
    RUB("RUB");

    private String value;

    private static final Map<String, Currency> CURRENCY_MAP = Arrays
            .stream(values())
            .collect(Collectors.toMap(Currency::getValue, Function.identity()));

    Currency(final String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue(){
        return value;
    }

    public static Currency getCurrencyByValue(final String value){
        return CURRENCY_MAP.get(value);
    }
}
