package com.artel.platform.service_rates.router;

import com.artel.platform.service_rates.exceptions.IllegalRoleForRequestException;
import com.artel.platform.service_rates.properties.CommonProperty;
import com.artel.platform.service_rates.util.UtilException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerFilterFunction;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RoleUserFilter implements HandlerFilterFunction<ServerResponse, ServerResponse> {

    private static final String RIGHT_ROLES_ERROR = "User doesn't have right";

    private final CommonProperty property;
    private final UtilException utilException;

    @Override
    public Mono<ServerResponse> filter(
            final ServerRequest request,
            final HandlerFunction<ServerResponse> handlerFunction
    ) {
        final var headers = request.headers().asHttpHeaders();
        final var roles = headers.get(property.getRoleAdmin());

        if (roles == null || roles.isEmpty()) {
            return getError(request);
        }
        final var role = roles.iterator().next();

        if (!role.equals(property.getRoleAdmin())) {
            return getError(request);
        }
        return handlerFunction.handle(request);
    }

    private Mono<ServerResponse> getError(final ServerRequest request){
        return utilException.getAttributesError(
                new IllegalRoleForRequestException(RIGHT_ROLES_ERROR),
                request);
    }
}
