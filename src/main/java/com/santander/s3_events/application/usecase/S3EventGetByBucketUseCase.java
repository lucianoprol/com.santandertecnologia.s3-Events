package com.santander.s3_events.application.usecase;

import org.springframework.stereotype.Component;

import com.santander.s3_events.domain.error.S3EventsNotFoundException;
import com.santander.s3_events.domain.model.S3Event;
import com.santander.s3_events.domain.port.S3EventQueryRepositoryPort;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

@Slf4j
@Component
@RequiredArgsConstructor
public class S3EventGetByBucketUseCase {

    private final S3EventQueryRepositoryPort repository;

    public Flux<S3Event> execute(
            String bucketName,
            int page,
            int size) {
        log.debug("Fetching events for bucket {}", bucketName);
        return repository.findByBucketName(bucketName, page, size)
                .switchIfEmpty(Flux.error(new S3EventsNotFoundException(bucketName)));
    }
}
