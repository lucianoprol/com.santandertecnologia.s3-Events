package com.santander.s3_events.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.santander.s3_events.api.dto.S3EventQueryResponse;
import com.santander.s3_events.application.usecase.S3EventGetByBucketUseCase;
import com.santander.s3_events.infrastructure.mapper.S3EventQueryApiMapper;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/s3-events")
@RequiredArgsConstructor
public class S3EventQueryController {

    private final S3EventGetByBucketUseCase useCase;
    private final S3EventQueryApiMapper mapper;

    @GetMapping("/{bucketName}")
    public Flux<S3EventQueryResponse> getByBucket(
            @PathVariable String bucketName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return useCase.execute(bucketName, page, size)
                .map(mapper::to);
    }
}