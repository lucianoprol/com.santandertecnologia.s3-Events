package com.santander.s3_events.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.santander.s3_events.api.dto.S3EventUpdateRequest;
import com.santander.s3_events.api.dto.S3EventUpdateResponse;
import com.santander.s3_events.application.usecase.S3EventUpdateUseCase;
import com.santander.s3_events.infrastructure.mapper.S3EventUpdateApiMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/s3-events")
@RequiredArgsConstructor
public class S3EventUpdateController {

    private final S3EventUpdateUseCase useCase;
    private final S3EventUpdateApiMapper mapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<S3EventUpdateResponse> create(
            @Valid @RequestBody Mono<S3EventUpdateRequest> requestMono) {
        return requestMono
                .map(mapper::to)
                .flatMap(useCase::execute)
                .map(mapper::to);
    }
}
