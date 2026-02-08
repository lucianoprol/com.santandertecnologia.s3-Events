package com.santander.s3_events.infrastructure.mongo;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.santander.s3_events.domain.model.S3Event;
import com.santander.s3_events.domain.port.S3EventQueryRepositoryPort;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Component
@RequiredArgsConstructor
public class S3EventMongoQueryAdapter
        implements S3EventQueryRepositoryPort {

    private final ReactiveMongoTemplate mongoTemplate;

    @Override
    public Flux<S3Event> findByBucketName(
            String bucketName,
            int page,
            int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "eventTime"));

        Query query = Query.query(
                Criteria.where("bucketName").is(bucketName)).with(pageable);

        return mongoTemplate.find(query, S3Event.class);
    }
}