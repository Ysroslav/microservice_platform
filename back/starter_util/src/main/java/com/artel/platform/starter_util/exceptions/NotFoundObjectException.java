package com.artel.platform.starter_util.exceptions;

import java.io.Serial;

public class NotFoundObjectException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = -4398825756528729304L;

    public NotFoundObjectException(String s, Class<?> object) {
        super(s + " object: " + object.getName());
    }
}
