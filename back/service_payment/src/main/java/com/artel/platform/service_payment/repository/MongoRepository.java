package com.artel.platform.service_payment.repository;

import com.mongodb.reactivestreams.client.MongoCollection;
import org.bson.Document;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface MongoRepository {

    <T> Mono<MongoCollection<Document>> createCollectionIfNotExist(final Class<? extends T> obj);

    <T> Mono<String> createIndex(final Class<? extends T> obj, final String fieldForIndex);

    <T> Mono<Void> dropCollection(final Class<? extends T> obj);

    <T> Mono<T> save(final T obj);

    <T> Flux<T> findAll(final Class<T> obj);

    <T> Mono<Boolean> collectionExist(final Class<T> obj);

    <T> Flux<String> getAllIndex(final Class<T> obj);

    <T> Flux<? extends T> findAllByField(final Class<T> obj, final String field, final String value);

    <T> Mono<T> put(final Class<T> clazz,
                                 final String fieldSearch,
                                 final Object valueSearch,
                                 final String fieldUpdate,
                                 final Object valueUpdate);


    <T> Mono<T> putMapFields(final Class<T> clazz,
                                          final String fieldSearch,
                                          final Object valueSearch,
                                          final Map<String, Object> mapFields
    );

    <T> Flux<? extends T> findBySeveralParameterOrderFieldDesc(final Class<T> obj,
                                                               final Map<String, Object> params,
                                                               final String fieldOrder);

    <T> Mono<T> saveIfExist(final T obj, Map<String, Object> params, Map<String, Object> paramsNot);
}
