package com.artel.platform.starter_util.exceptions;

import java.io.Serial;

public class IllegalParamInRequestException extends RuntimeException {


    @Serial
    private static final long serialVersionUID = -5022522809546766957L;

    public IllegalParamInRequestException (String s, Class<?> object, String methodName){
        super(s + " object: " + object + " method:" + methodName);
    }
}
