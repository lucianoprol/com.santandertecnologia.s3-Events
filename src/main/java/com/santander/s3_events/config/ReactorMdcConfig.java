package com.santander.s3_events.config;


import org.reactivestreams.Subscription;
import org.slf4j.MDC;
import org.springframework.context.annotation.Configuration;

import com.santander.s3_events.api.filter.TraceIdWebFilter;

import jakarta.annotation.PostConstruct;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Operators;
import reactor.util.context.Context;

@Configuration
public class ReactorMdcConfig {

    @PostConstruct
    public void setup() {

        Hooks.onEachOperator("mdc", Operators.lift(
            (scannable, subscriber) ->
                new CoreSubscriber<>() {

                    @Override
                    public void onSubscribe(Subscription s) {
                        subscriber.onSubscribe(s);
                    }

                    @Override
                    public void onNext(Object o) {
                        copyToMdc(subscriber.currentContext());
                        subscriber.onNext(o);
                    }

                    @Override
                    public void onError(Throwable t) {
                        copyToMdc(subscriber.currentContext());
                        subscriber.onError(t);
                    }

                    @Override
                    public void onComplete() {
                        copyToMdc(subscriber.currentContext());
                        subscriber.onComplete();
                    }
                }
        ));
    }

    private void copyToMdc(Context context) {
        if (context.hasKey(TraceIdWebFilter.TRACE_ID)) {
            MDC.put(
                TraceIdWebFilter.TRACE_ID,
                context.get(TraceIdWebFilter.TRACE_ID)
            );
        }
    }
}