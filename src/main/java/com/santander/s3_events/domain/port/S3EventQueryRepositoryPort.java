package com.santander.s3_events.domain.port;

import com.santander.s3_events.domain.model.S3Event;

import reactor.core.publisher.Flux;

public interface S3EventQueryRepositoryPort {

    Flux<S3Event> findByBucketName(
            String bucketName,
            int page,
            int size);
}
