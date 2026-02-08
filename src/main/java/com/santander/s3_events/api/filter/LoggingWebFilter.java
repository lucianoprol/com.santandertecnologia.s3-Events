package com.santander.s3_events.api.filter;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Component
@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
public class LoggingWebFilter implements WebFilter {

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            WebFilterChain chain) {

        long startTime = System.currentTimeMillis();
        ServerHttpRequest request = exchange.getRequest();

        logRequest(request);

        return chain.filter(exchange)
                .doOnSuccess(aVoid -> logResponse(exchange, startTime))
                .doOnError(error -> logError(exchange, error, startTime));
    }

    private void logRequest(ServerHttpRequest request) {
        log.info(
                "Incoming request: method={} path={} query={}",
                request.getMethod(),
                request.getURI().getPath(),
                request.getURI().getQuery());
    }

    private void logResponse(
            ServerWebExchange exchange,
            long startTime) {
        long duration = System.currentTimeMillis() - startTime;

        log.info(
                "Request completed: status={} duration={}ms",
                exchange.getResponse().getStatusCode(),
                duration);
    }

    private void logError(
            ServerWebExchange exchange,
            Throwable error,
            long startTime) {
        long duration = System.currentTimeMillis() - startTime;

        log.error(
                "Request failed: status={} duration={}ms error={}",
                exchange.getResponse().getStatusCode(),
                duration,
                error.getMessage(),
                error);
    }
}