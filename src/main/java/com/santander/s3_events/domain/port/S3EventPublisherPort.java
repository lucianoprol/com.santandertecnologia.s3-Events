package com.santander.s3_events.domain.port;

import com.santander.s3_events.domain.model.S3Event;

import reactor.core.publisher.Mono;

public interface S3EventPublisherPort {
    Mono<Void> publish(S3Event event);
}
