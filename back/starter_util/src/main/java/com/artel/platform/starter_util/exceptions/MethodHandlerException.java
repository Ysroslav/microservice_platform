package com.artel.platform.starter_util.exceptions;

import java.io.Serial;

public class MethodHandlerException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = -7821929537053666662L;

    public MethodHandlerException(String s, Class<?> object, String methodName) {
        super(s + " object: " + object.getName() + " method:" + methodName);
    }
}
