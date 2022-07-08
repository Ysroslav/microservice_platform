package com.artel.platform.service_payment.repository.impl;

import com.artel.platform.service_payment.property.MongoProperty;
import com.artel.platform.service_payment.repository.MongoRepository;
import com.mongodb.reactivestreams.client.MongoCollection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.data.mongodb.core.index.IndexInfo;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MongoRepositoryImpl implements MongoRepository {

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public final <T> Mono<MongoCollection<Document>> createCollectionIfNotExist(final Class<? extends T> obj){
        return reactiveMongoTemplate.collectionExists(obj)
                                    .publishOn(Schedulers.boundedElastic())
                                    .flatMap(result -> result ? Mono.empty() : createCollection(obj) );
    }


    private <T> Mono<MongoCollection<Document>> createCollection(final Class<? extends T> obj) {
        return reactiveMongoTemplate.createCollection(
                obj,
                CollectionOptions.empty());
    }

    @Override
    public final <T> Mono<String> createIndex(final Class<? extends T> obj, final String fieldForIndex) {
        return reactiveMongoTemplate.indexOps(obj)
                                    .ensureIndex(new Index().on(fieldForIndex, Sort.Direction.ASC));
    }

    @Override
    public final <T> Mono<Void> dropCollection(final Class<? extends T> obj) {
        return reactiveMongoTemplate.dropCollection(obj);
    }

    @Override
    public final <T> Mono<T> save(final T obj){
        return reactiveMongoTemplate.save(obj);
    }

    @Override
    public final <T> Flux<T> findAll(final Class<T> obj) {
        return reactiveMongoTemplate.findAll(obj);
    }

    @Override
    public final <T> Mono<Boolean> collectionExist(final Class<T> obj) {return reactiveMongoTemplate.collectionExists(obj);}

    @Override
    public final <T> Flux<String> getAllIndex(final Class<T> obj) {
        return reactiveMongoTemplate.indexOps(obj).getIndexInfo().map(IndexInfo::getName);
    }

    @Override
    public final <T> Flux<? extends T> findAllByField(final Class<T> obj, final String field, final String value){
        final var query = new Query();
        query.addCriteria(Criteria.where(field).is(value));
        return reactiveMongoTemplate.find(query, obj);
    }



    @Override
    public final <T> Mono<T> put(final Class<T> clazz,
                                 final String fieldSearch,
                                 final Object valueSearch,
                                 final String fieldUpdate,
                                 final Object valueUpdate){
        final var query = new Query();
        query.addCriteria(Criteria.where(fieldSearch).is(valueSearch));
        final Update updateFields = new Update();
        updateFields.set(fieldUpdate, valueUpdate);
        return reactiveMongoTemplate.findAndModify(
                query, updateFields,
                new FindAndModifyOptions().returnNew(true),
                clazz);
    }

    @Override
    public final <T> Mono<T> putMapFields(final Class<T> clazz,
                                          final String fieldSearch,
                                          final Object valueSearch,
                                          final Map<String, Object> mapFields
                                          ){
        final var query = new Query();
        query.addCriteria(Criteria.where(fieldSearch).is(valueSearch));
        final Update updateFields = new Update();
        mapFields.forEach(updateFields::set);
        return reactiveMongoTemplate.findAndModify(
                query, updateFields,
                new FindAndModifyOptions().returnNew(true),
                clazz);
    }


    @Override
    public final <T> Flux<? extends T> findBySeveralParameterOrderFieldDesc(final Class<T> obj,
                                                                            final Map<String, Object> params,
                                                                            final String fieldOrder){
        final var query = new Query();
        params.forEach((key, value) -> query.addCriteria(Criteria.where(key).is(value)));
        query.with(Sort.by(Sort.Direction.DESC, fieldOrder));
        return reactiveMongoTemplate.find(query, obj);
    }


    @Override
    public final <T> Mono<T> saveIfExist(final T obj, Map<String, Object> params, Map<String, Object> paramsNot){
        final var query = new Query();
        params.forEach((key, value) -> query.addCriteria(Criteria.where(key).is(value)));
        paramsNot.forEach((key, value) -> query.addCriteria(Criteria.where(key).ne(value)));
        return reactiveMongoTemplate.find(query, obj.getClass())
                .collectList()
                .flatMap(res -> {
                    if (res.isEmpty()){
                        return reactiveMongoTemplate.save(obj);
                    }
                    return Mono.error(new RuntimeException("Payment with exists"));
                });
    }


}
