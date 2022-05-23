package com.artel.platform.service_rates.exceptions;

import java.io.Serial;

public class IllegalRateIdException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 4914432822787604573L;

    public IllegalRateIdException(String s) {
        super(s);
    }
}
