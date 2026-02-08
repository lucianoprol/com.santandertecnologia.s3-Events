package com.santander.s3_events.infrastructure.mongo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface S3EventMongoRepository extends ReactiveMongoRepository<S3EventDocument, String> {

}
