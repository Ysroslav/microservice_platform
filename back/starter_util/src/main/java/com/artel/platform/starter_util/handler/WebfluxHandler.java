package com.artel.platform.starter_util.handler;

import com.artel.platform.starter_util.properties.WebfluxProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.Duration;

public class WebfluxHandler {

    @Autowired
    private WebfluxProperties properties;

    public <T> Mono<T> handleRequestReactive(Mono<T> input){
        return input.timeout(Duration.ofMillis(properties.getTimeout()))
                    .retryWhen(Retry.backoff(properties.getRetryTimes(), Duration.ofMillis(properties.getRetryPeriod())))
                    .subscribeOn(Schedulers.boundedElastic());
    }

    public <T> Mono<T> handleRequestReactiveInSameThread(Mono<T> input){
        return input.timeout(Duration.ofMillis(properties.getTimeout()))
                    .retryWhen(Retry.backoff(properties.getRetryTimes(), Duration.ofMillis(properties.getRetryPeriod())));
    }

    public <T> Flux<T> handleRequestReactive(Flux<T> input){
        return input.timeout(Duration.ofMillis(properties.getTimeout()))
                    .retryWhen(Retry.backoff(properties.getRetryTimes(), Duration.ofMillis(properties.getRetryPeriod())))
                    .subscribeOn(Schedulers.boundedElastic());
    }
}
