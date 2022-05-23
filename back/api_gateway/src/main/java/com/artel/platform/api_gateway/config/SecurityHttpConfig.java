package com.artel.platform.api_gateway.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityHttpConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
                .authorizeExchange()
                .pathMatchers("/rate/**")
                .permitAll()
                .and()
                .authorizeExchange()
                .pathMatchers("/artist/**")
                .hasAnyRole("ARTIST")
                .and().csrf().disable()
                .oauth2Login()
                //.authenticationSuccessHandler(requestModification)
                .and().oauth2ResourceServer().jwt();// to redirect to oauth2 login page.
        return http.build();
    }
}
