package com.santander.s3_events.domain.error;

public class S3EventsNotFoundException extends DomainException {

    public S3EventsNotFoundException(String bucketName) {
        super("No events found for bucket: " + bucketName);
    }
}
