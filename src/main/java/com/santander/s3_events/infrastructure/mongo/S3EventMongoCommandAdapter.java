package com.santander.s3_events.infrastructure.mongo;

import org.springframework.stereotype.Component;

import com.santander.s3_events.domain.model.S3Event;
import com.santander.s3_events.domain.port.S3EventCommandRepositoryPort;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class S3EventMongoCommandAdapter implements S3EventCommandRepositoryPort {

    private final S3EventMongoRepository repository;

    @Override
    public Mono<S3Event> save(S3Event event) {

        S3EventDocument doc = new S3EventDocument();
        doc.setBucketName(event.getBucketName());
        doc.setObjectKey(event.getObjectKey());
        doc.setEventType(event.getEventType());
        doc.setEventTime(event.getEventTime());
        doc.setObjectSize(event.getObjectSize());

        return repository.save(doc)
                .map(saved -> S3Event.builder()
                        .id(saved.getId())
                        .bucketName(saved.getBucketName())
                        .objectKey(saved.getObjectKey())
                        .eventType(saved.getEventType())
                        .eventTime(saved.getEventTime())
                        .objectSize(saved.getObjectSize())
                        .build());
    }
}