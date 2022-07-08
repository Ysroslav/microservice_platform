package com.artel.platform.service_payment.config;

import com.artel.platform.service_payment.property.CommonProperty;
import io.netty.channel.ChannelOption;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private final CommonProperty commonProperty;

    @Bean
    public WebClient webClient() {
        final HttpClient client = HttpClient.create()
                                            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, commonProperty.getPlatformTimeout())
                                            .responseTimeout(Duration.ofNanos(commonProperty.getPlatformTimeout()))
                                            .followRedirect((req, res) -> HttpResponseStatus.FOUND.equals(res.status()))
                                            .doOnConnected(conn ->
                                                    conn.addHandlerLast(new ReadTimeoutHandler(commonProperty.getPlatformTimeout(), TimeUnit.MILLISECONDS))
                                                        .addHandlerLast(new WriteTimeoutHandler(commonProperty.getPlatformTimeout(), TimeUnit.MILLISECONDS)));
        return WebClient.builder()
                        .baseUrl(commonProperty.getPathPlatform())
                        .clientConnector(new ReactorClientHttpConnector(client))
                        .build();
    }
}
