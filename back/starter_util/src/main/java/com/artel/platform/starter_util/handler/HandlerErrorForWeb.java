package com.artel.platform.starter_util.handler;

import com.artel.platform.starter_util.exceptions.IllegalRightsHeadersInRequestException;
import com.artel.platform.starter_util.exceptions.IllegalRoleForRequestException;
import com.artel.platform.starter_util.exceptions.NotFoundObjectException;
import com.artel.platform.starter_util.exceptions.ParseDataConvertException;
import com.artel.platform.starter_util.exceptions.ValidationRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;

@Slf4j
public class HandlerErrorForWeb {

    public Mono<ServerResponse> getAttributesError(
           final  Throwable serverException,
           final  ServerRequest request
    ) {
        log.error("Error! {} message: {}", serverException, serverException.getMessage());
        final var map = new HashMap<String, Object>();
        map.put("status", getStatusWithException(serverException));
        map.put("error", serverException.toString());
        map.put("message", serverException.getMessage());
        map.put("timestamp", LocalDateTime.now());
        map.put("path", request.path());

        return ServerResponse.status(getStatusWithException(serverException))
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(BodyInserters.fromValue(map));

    }

    private HttpStatus getStatusWithException(
            final Throwable serverException
    ) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (serverException instanceof IllegalRightsHeadersInRequestException) {
            status = HttpStatus.FORBIDDEN;
        }

        if (serverException instanceof IllegalRoleForRequestException) {
            status = HttpStatus.FORBIDDEN;
        }

        if (serverException instanceof ParseDataConvertException) {
            status = HttpStatus.BAD_REQUEST;
        }

        if (serverException instanceof NotFoundObjectException) {
            status = HttpStatus.NOT_FOUND;
        }

        if (serverException instanceof NullPointerException) {
            status = HttpStatus.NOT_FOUND;
        }

        if (serverException instanceof ValidationRequestException) {
            status = HttpStatus.BAD_REQUEST;
        }

        return status;
    }

    public static <T > void validateObject(T target, Validator validator){
        final Errors errors = new BeanPropertyBindingResult(
                target,
                target.getClass().getName());
        validator.validate(target, errors);
        if (!errors.getAllErrors().isEmpty()){
            throw new ValidationRequestException("Payment not validate");
        }
    }
}
