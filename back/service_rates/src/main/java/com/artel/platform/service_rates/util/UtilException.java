package com.artel.platform.service_rates.util;

import com.artel.platform.service_rates.exceptions.IllegalRateIdException;
import com.artel.platform.service_rates.exceptions.IllegalRoleForRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class UtilException {

    public Mono<ServerResponse> getAttributesError(
            Throwable serverException,
            ServerRequest request
    ) {
        log.error("Error! {} message: {}", serverException, serverException.getMessage());
        final var mapError = getErrorMap(
                serverException,
                request,
                getStatusWithException(serverException)
        );
        return ServerResponse.status(getStatusWithException(serverException))
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(BodyInserters.fromValue(mapError));

    }

    public Mono<ServerResponse> getAttributesError(
            Throwable serverException,
            ServerRequest request,
            HttpStatus status
    ) {

        log.error("Error! {} message: {}", serverException, serverException.getMessage());
        final var mapError = getErrorMap(
                serverException,
                request,
                status
        );
        return ServerResponse.status(status)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(BodyInserters.fromValue(mapError));

    }

    private Map<String, Object> getErrorMap(
            Throwable serverException,
            ServerRequest request,
            HttpStatus status
    ) {
        final var map = new HashMap<String, Object>();
        map.put("status", status);
        map.put("error", serverException.toString());
        map.put("message", serverException.getMessage());
        map.put("timestamp", LocalDateTime.now());
        map.put("path", request.path());
        return map;
    }

    private HttpStatus getStatusWithException(
            final Throwable serverException
    ) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (serverException instanceof IllegalRoleForRequestException) {
            status = HttpStatus.FORBIDDEN;
        }

        if (serverException instanceof IllegalRateIdException) {
            status = HttpStatus.BAD_REQUEST;
        }
        return status;
    }
}
