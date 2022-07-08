package com.artel.platform.service_payment.property;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class MongoProperty {

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Value("${spring.data.mongodb.uri}")
    private String uri;

    @Value("${spring.data.mongodb.size}")
    private int size;

    @Value("${spring.data.mongodb.maxDocument}")
    private int maxDocument;

    @Value("${spring.data.mongodb.username}")
    private String user;

    @Value("${spring.data.mongodb.password}")
    private String password;

    @Value("${spring.data.mongodb.connectTimeout}")
    private int connectTimeout;

    @Value("${spring.data.mongodb.readTimeout}")
    private int readTimeout;

    @Value("${spring.data.mongodb.serverSelectTimeout}")
    private int serverSelectTimeout;
}
