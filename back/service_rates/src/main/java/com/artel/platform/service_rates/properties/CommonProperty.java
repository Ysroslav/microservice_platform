package com.artel.platform.service_rates.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class CommonProperty {

    @Value("${spring.webflux.timeout}")
    private int timeout;

    @Value("${spring.webflux.retryTimes}")
    private int retryTimes;

    @Value("${spring.webflux.retryPeriod}")
    private int retryPeriod;

    @Value("${basepath.common}")
    private String basePathCommon;

    @Value("${basepath.admin}")
    private String basePathAdmin;

    @Value("${role}")
    private String roleAdmin;
}
