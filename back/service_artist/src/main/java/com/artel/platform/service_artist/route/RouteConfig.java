package com.artel.platform.service_artist.route;

import com.artel.platform.service_artist.handler.ArtistHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RouteConfig {

    @Bean
    public RouterFunction<ServerResponse> route(final ArtistHandler handler) {
        return RouterFunctions
                .route(RequestPredicates.GET("/artist/principal").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::getArtistPrincipal)
                .andRoute(RequestPredicates.GET("/artist/check").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), handler::checkAuthorization);
    }
}
