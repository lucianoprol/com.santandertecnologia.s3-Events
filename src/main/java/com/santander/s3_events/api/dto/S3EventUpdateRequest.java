package com.santander.s3_events.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class S3EventUpdateRequest {

    @NotBlank
    private String bucketName;

    @NotBlank
    private String objectKey;

    @NotBlank
    private String eventType;

    @NotNull
    private Instant eventTime;

    @NotNull
    private Long objectSize;
}