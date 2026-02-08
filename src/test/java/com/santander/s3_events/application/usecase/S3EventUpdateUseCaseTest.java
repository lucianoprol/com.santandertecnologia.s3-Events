package com.santander.s3_events.application.usecase;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Instant;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.santander.s3_events.domain.model.S3Event;
import com.santander.s3_events.domain.port.S3EventCommandRepositoryPort;
import com.santander.s3_events.domain.port.S3EventPublisherPort;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class S3EventUpdateUseCaseTest {

    @Mock
    S3EventCommandRepositoryPort repository;

    @Mock
    S3EventPublisherPort publisher;

    S3EventUpdateUseCase useCase;

    @BeforeEach
    void setup() {
        useCase = new S3EventUpdateUseCase(repository, publisher);
    }

    @Test
    void shouldSaveEventAndPublishMessage() {

        when(repository.save(any()))
                .thenAnswer(inv -> Mono.just(generatedEvent()));

        when(publisher.publish(any()))
                .thenReturn(Mono.empty());

        StepVerifier.create(
                useCase.execute(event()))
                .expectNextMatches(event -> event.getId().equals("generated-id"))
                .verifyComplete();

        verify(repository).save(any());
        verify(publisher).publish(any());
    }

    private S3Event event() {
        return S3Event.builder()
                .bucketName("bucket")
                .objectKey("file.csv")
                .eventType("OBJECT_CREATED")
                .eventTime(Instant.now())
                .objectSize(200L)
                .build();
    }

    private S3Event generatedEvent() {
        return S3Event.builder()
                .id("generated-id")
                .build();
    }
}
