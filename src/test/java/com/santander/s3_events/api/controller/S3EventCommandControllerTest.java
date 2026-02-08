package com.santander.s3_events.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.santander.s3_events.api.dto.S3EventUpdateRequest;
import com.santander.s3_events.application.usecase.S3EventUpdateUseCase;
import com.santander.s3_events.domain.model.S3Event;

import reactor.core.publisher.Mono;

@WebFluxTest(controllers = S3EventUpdateController.class)
class S3EventCommandControllerTest {

    @MockitoBean
    S3EventUpdateUseCase useCase;

    @Autowired
    WebTestClient webTestClient;

    @Test
    void shouldCreateEventSuccessfully() {

        when(useCase.execute(any()))
                .thenReturn(Mono.just(event()));

        webTestClient.post()
                .uri("/api/v1/s3-events")
                .bodyValue(request())
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").exists();
    }

    private S3EventUpdateRequest request() {
        return new S3EventUpdateRequest(
                "bucket",
                "file.csv",
                "OBJECT_CREATED",
                Instant.now(),
                300L);
    }

    private S3Event event() {
        return S3Event.builder()
                .id("id-123")
                .bucketName("bucket")
                .objectKey("file.csv")
                .eventType("OBJECT_CREATED")
                .eventTime(Instant.now())
                .objectSize(300L)
                .build();
    }
}