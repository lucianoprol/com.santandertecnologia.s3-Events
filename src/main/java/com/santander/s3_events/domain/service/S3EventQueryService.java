package com.santander.s3_events.domain.service;

import com.santander.s3_events.domain.model.S3Event;
import com.santander.s3_events.domain.port.S3EventQueryRepositoryPort;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class S3EventQueryService {

    private final S3EventQueryRepositoryPort repository;

    public Flux<S3Event> getEventsByBucket(
            String bucketName,
            int page,
            int size) {
        return repository.findByBucketName(bucketName, page, size);
    }
}