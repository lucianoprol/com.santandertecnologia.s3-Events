package com.santander.s3_events.api.error;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.santander.s3_events.domain.error.DomainException;
import com.santander.s3_events.domain.error.S3EventsNotFoundException;

import reactor.core.publisher.Mono;

@RestControllerAdvice
public class GlobalErrorHandler {

    @ExceptionHandler(DomainException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleDomain(
            DomainException ex) {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());
        return Mono.just(
                new ResponseEntity<>(
                        new ErrorResponse(HttpStatus.BAD_REQUEST.getReasonPhrase().toUpperCase(),
                                "DOMAIN_ERROR",
                                HttpStatus.BAD_REQUEST.value(),
                                errors),
                        HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(S3EventsNotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleNotFound(
            DomainException ex) {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());
        return Mono.just(
                new ResponseEntity<>(
                        new ErrorResponse(HttpStatus.NOT_FOUND.getReasonPhrase().toUpperCase(),
                                "DATA NOT FOUND",
                                HttpStatus.NOT_FOUND.value(),
                                errors),
                        HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(Throwable.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGeneric(
            Throwable ex) {
        List<String> errors = new ArrayList<>();
        errors.add(ex.getMessage());
        return Mono.just(
                new ResponseEntity<>(
                        new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase().toUpperCase(),
                                "DATA NOT FOUND",
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                errors),
                        HttpStatus.INTERNAL_SERVER_ERROR));
    }
}
