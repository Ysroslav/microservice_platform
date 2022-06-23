package com.artel.platform.service_artist.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.security.Principal;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArtistHandler {

    public Mono<ServerResponse> getArtistPrincipal(final ServerRequest request) {
        log.info("test {}", request.headers());
        final var jwt = request.headers().firstHeader("Authorization");
        if (jwt == null) {
            throw new NullPointerException("jwt token for user is null");
        }
        log.info("jwt token for user {}", jwt);
        return request.principal().flatMap(principal -> ServerResponse.ok().bodyValue(principal.getName()));
    }

    public Mono<ServerResponse> checkAuthorization(final ServerRequest request){
        return ServerResponse.ok().bodyValue("TEST")
                             .subscribeOn(Schedulers.boundedElastic());
    }
}
