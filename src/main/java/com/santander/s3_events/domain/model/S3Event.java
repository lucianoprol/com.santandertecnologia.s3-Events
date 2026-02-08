package com.santander.s3_events.domain.model;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class S3Event {

    String id;
    String bucketName;
    String objectKey;
    String eventType;
    Instant eventTime;
    Long objectSize;
}