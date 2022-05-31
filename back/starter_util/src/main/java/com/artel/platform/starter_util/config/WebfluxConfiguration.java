package com.artel.platform.starter_util.config;

import com.artel.platform.starter_util.handler.WebfluxHandler;
import com.artel.platform.starter_util.properties.WebfluxProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(WebfluxProperties.class)
public class WebfluxConfiguration {


    @Bean
    public WebfluxHandler webfluxHandler(){
        return new WebfluxHandler();
    }

}
