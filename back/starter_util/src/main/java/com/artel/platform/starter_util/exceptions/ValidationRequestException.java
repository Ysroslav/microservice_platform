package com.artel.platform.starter_util.exceptions;

import java.io.Serial;

public class ValidationRequestException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 7574670476699980961L;

    public ValidationRequestException(String s){
        super(s);
    }
}
