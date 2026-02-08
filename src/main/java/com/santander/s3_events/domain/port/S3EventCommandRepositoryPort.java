package com.santander.s3_events.domain.port;

import com.santander.s3_events.domain.model.S3Event;

import reactor.core.publisher.Mono;

public interface S3EventCommandRepositoryPort {

    Mono<S3Event> save(S3Event event);
}
