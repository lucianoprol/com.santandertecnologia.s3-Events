package com.santander.s3_events.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.santander.s3_events.domain.port.S3EventPublisherPort;
import com.santander.s3_events.domain.port.S3EventCommandRepositoryPort;
import com.santander.s3_events.domain.service.S3EventUpdateService;

@Configuration
public class S3EventServiceConfig {

    @Bean
    S3EventUpdateService s3EventService(
            S3EventCommandRepositoryPort repository,
            S3EventPublisherPort publisher) {
        return new S3EventUpdateService(repository, publisher);
    }
}