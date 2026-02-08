package com.santander.s3_events.infrastructure.mapper;

import org.springframework.stereotype.Component;

import com.santander.s3_events.api.dto.S3EventUpdateRequest;
import com.santander.s3_events.api.dto.S3EventUpdateResponse;
import com.santander.s3_events.domain.model.S3Event;

@Component
public class S3EventUpdateApiMapper {

    /**
     * Mapeo de S3EventRequest a model de S3Event
     * 
     * @param request
     *                el S3EventRequest a mapear
     * @return el model de S3Event mapeado
     */
    public S3Event to(S3EventUpdateRequest request) {
        return S3Event.builder()
                .bucketName(request.getBucketName())
                .objectKey(request.getObjectKey())
                .eventType(request.getEventType())
                .eventTime(request.getEventTime())
                .objectSize(request.getObjectSize())
                .build();
    }

    /**
     * Mapeo del model de S3Event a S3EventUpdateResponse
     * 
     * @param event
     *              el model de S3Event a mapear
     * @return el S3EventUpdateResponse mapeado
     */
    public S3EventUpdateResponse to(S3Event event) {
        return new S3EventUpdateResponse(
                event.getId());
    }
}
