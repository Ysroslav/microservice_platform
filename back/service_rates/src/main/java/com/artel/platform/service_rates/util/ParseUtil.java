package com.artel.platform.service_rates.util;

import java.math.BigDecimal;

public final class ParseUtil {

    public static BigDecimal convertStringToBigDecimal(final String str) {
       return new BigDecimal(str);
    }
}
