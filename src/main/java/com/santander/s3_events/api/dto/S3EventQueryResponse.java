package com.santander.s3_events.api.dto;

import java.time.Instant;

public record S3EventQueryResponse(
        String id,
        String objectKey,
        String eventType,
        Instant eventTime,
        Long objectSize) {
}