package com.santander.s3_events.api.filter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TraceIdWebFilter implements WebFilter {

    public static final String TRACE_ID = "traceId";
    private static final String HEADER = "X-Trace-Id";
    private static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    @Override
    public Mono<Void> filter(
            ServerWebExchange exchange,
            WebFilterChain chain) {

        String traceId = Optional
                .ofNullable(exchange.getRequest()
                        .getHeaders()
                        .getFirst(HEADER))
                .orElse(generateTraceId());

        return chain.filter(exchange)
                .contextWrite(ctx -> ctx.put(TRACE_ID, traceId));
    }

    /**
     * Generador de Trace ID random
     * 
     * @return String de 22 caracteres: 5 letras y luego la fecha
     */
    private static String generateTraceId() {
        StringBuilder traceId = new StringBuilder();
        Random random = new Random();
        // Primero 5 letras random
        for (int i = 0; i < 5; i++) {
            traceId.append(LETTERS.charAt(random.nextInt(LETTERS.length())));
        }
        // Luego la fecha actual
        Date date = new Date();
        String dateId = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(date);
        traceId.append(dateId);
        return traceId.toString();
    }
}