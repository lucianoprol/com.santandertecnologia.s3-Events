package com.santander.s3_events.application.usecase;

import org.springframework.stereotype.Component;

import com.santander.s3_events.domain.model.S3Event;
import com.santander.s3_events.domain.port.S3EventCommandRepositoryPort;
import com.santander.s3_events.domain.port.S3EventPublisherPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3EventUpdateUseCase {

    private final S3EventCommandRepositoryPort repository;
    private final S3EventPublisherPort publisher;

    public Mono<S3Event> execute(S3Event event) {
        log.debug("Saving event {}", event);
        return repository.save(event)
                .flatMap(saved -> publisher.publish(saved)
                        .thenReturn(saved));
    }
}
