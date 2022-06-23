package com.artel.platform.starter_util.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("util.webflux")
public class WebfluxProperties {
    private int timeout;

    private int retryTimes;

    private int retryPeriod;
}
