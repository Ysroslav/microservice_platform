package com.artel.platform.service_rates.exceptions;

import java.io.Serial;

public class ParseDataConvertException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 1836904339276987190L;

    public ParseDataConvertException(String s) {
        super(s);
    }
}
