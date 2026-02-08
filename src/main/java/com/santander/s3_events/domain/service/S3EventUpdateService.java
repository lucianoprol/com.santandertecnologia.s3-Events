package com.santander.s3_events.domain.service;

import com.santander.s3_events.domain.model.S3Event;
import com.santander.s3_events.domain.port.S3EventPublisherPort;
import com.santander.s3_events.domain.port.S3EventCommandRepositoryPort;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class S3EventUpdateService {

    private final S3EventCommandRepositoryPort repository;
    private final S3EventPublisherPort publisher;

    public Mono<S3Event> register(S3Event event) {

        return repository.save(event)
                .flatMap(saved ->
                        publisher.publish(saved)
                                .thenReturn(saved)
                );
    }
}