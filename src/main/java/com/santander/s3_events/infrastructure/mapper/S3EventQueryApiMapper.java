package com.santander.s3_events.infrastructure.mapper;

import org.springframework.stereotype.Component;

import com.santander.s3_events.api.dto.S3EventQueryResponse;
import com.santander.s3_events.domain.model.S3Event;

@Component
public class S3EventQueryApiMapper {

        /**
     * Mapeo del model de S3Event a S3EventQueryResponse
     * 
     * @param event
     *              el model de S3Event a mapear
     * @return el S3EventQueryResponse mapeado
     */
    public S3EventQueryResponse to(S3Event event) {
        return new S3EventQueryResponse(
                event.getId(),
                event.getObjectKey(),
                event.getEventType(),
                event.getEventTime(),
                event.getObjectSize());
    }
}
