package com.santander.s3_events.application.usecase;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.santander.s3_events.domain.error.S3EventsNotFoundException;
import com.santander.s3_events.domain.model.S3Event;
import com.santander.s3_events.domain.port.S3EventQueryRepositoryPort;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class S3EventGetByBucketUseCaseTest {

    @Mock
    S3EventQueryRepositoryPort repository;

    S3EventGetByBucketUseCase useCase;

    @BeforeEach
    void setup() {
        useCase = new S3EventGetByBucketUseCase(repository);
    }

    @Test
    void shouldReturnEventsWhenFound() {

        when(repository.findByBucketName("bucket", 0, 2))
                .thenReturn(Flux.just(event("1"), event("2")));

        StepVerifier.create(
                useCase.execute("bucket", 0, 2))
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    void shouldFailWithDomainErrorWhenNoEventsFound() {

        when(repository.findByBucketName(any(), anyInt(), anyInt()))
                .thenReturn(Flux.empty());

        StepVerifier.create(
                useCase.execute("bucket", 0, 10))
                .expectError(S3EventsNotFoundException.class)
                .verify();
    }

    private S3Event event(String id) {
        return S3Event.builder()
                .id(id)
                .bucketName("bucket")
                .objectKey("file.csv")
                .eventType("OBJECT_CREATED")
                .eventTime(Instant.now())
                .objectSize(100L)
                .build();
    }
}
