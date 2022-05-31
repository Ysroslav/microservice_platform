package com.artel.platform.starter_util.config;

import com.artel.platform.starter_util.handler.HandlerErrorForWeb;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ErrorHandlerConfiguration {

    @Bean
    public HandlerErrorForWeb handlerErrorForWeb(){
        return new HandlerErrorForWeb();
    }
}
