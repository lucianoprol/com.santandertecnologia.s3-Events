package com.santander.s3_events.infrastructure.sqs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.santander.s3_events.domain.model.S3Event;
import com.santander.s3_events.domain.port.S3EventPublisherPort;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class S3EventSqsAdapter implements S3EventPublisherPort {

    private final SqsAsyncClient sqsClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${aws.sqs.queue-url}")
    private String queueUrl;

    @Override
    public Mono<Void> publish(S3Event event) {

        return Mono.fromCallable(() -> objectMapper.writeValueAsString(event))
                .flatMap(body -> Mono.fromFuture(
                        sqsClient.sendMessage(
                                SendMessageRequest.builder()
                                        .queueUrl(queueUrl)
                                        .messageBody(body)
                                        .build())))
                .then();
    }
}