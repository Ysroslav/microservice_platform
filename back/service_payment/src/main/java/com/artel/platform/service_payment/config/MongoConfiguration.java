package com.artel.platform.service_payment.config;

import com.artel.platform.service_payment.property.MongoProperty;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.ReactiveMongoTransactionManager;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.reactive.TransactionalOperator;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@Configuration
@RequiredArgsConstructor
public class MongoConfiguration {

    private final MongoProperty mongoProperty;

    @Bean
    public MongoClient mongoClient(){

        return MongoClients.create(
                MongoClientSettings.builder()
                                   .applyConnectionString(new ConnectionString(mongoProperty.getUri()))
                                   .applyToClusterSettings(builder -> {
                                       builder.serverSelectionTimeout(
                                               mongoProperty.getServerSelectTimeout(), MILLISECONDS);
                                   })
                                   .applyToSocketSettings(builder -> {
                                       builder.connectTimeout(mongoProperty.getConnectTimeout(), MILLISECONDS);
                                       builder.readTimeout(mongoProperty.getReadTimeout(), MILLISECONDS);
                                   })
                                   .build());
    }

    @Bean
    public ReactiveMongoTemplate reactiveMongoTemplate() {
        return new ReactiveMongoTemplate(mongoClient(), mongoProperty.getDatabaseName());
    }

    @Bean
    public TransactionalOperator transactionalOperator(
            final ReactiveTransactionManager reactiveTransactionManager) {
        return TransactionalOperator.create(reactiveTransactionManager);
    }

    @Bean
    public ReactiveTransactionManager reactiveTransactionManager(
            final ReactiveMongoDatabaseFactory reactiveMongoDatabaseFactory){
        return new ReactiveMongoTransactionManager(reactiveMongoDatabaseFactory);
    }
}
