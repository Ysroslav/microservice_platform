package com.artel.platform.starter_util.exceptions;

import java.io.Serial;

public class IllegalRightsHeadersInRequestException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 3363528986185535281L;

    public IllegalRightsHeadersInRequestException(String s) {
        super(s);
    }
}
