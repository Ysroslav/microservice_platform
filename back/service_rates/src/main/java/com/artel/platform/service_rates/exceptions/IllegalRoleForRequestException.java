package com.artel.platform.service_rates.exceptions;

import java.io.Serial;

public class IllegalRoleForRequestException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 8943464001705574682L;

    public IllegalRoleForRequestException(String s) {
        super(s);
    }
}
