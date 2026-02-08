package com.santander.s3_events.infrastructure.mongo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document(collection = "s3_events")
class S3EventDocument {

    @Id
    private String id;
    private String bucketName;
    private String objectKey;
    private String eventType;
    private Instant eventTime;
    private Long objectSize;
}
