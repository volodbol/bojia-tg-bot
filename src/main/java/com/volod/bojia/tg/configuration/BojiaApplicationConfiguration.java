package com.volod.bojia.tg.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.annotation.EnableAsync;

import java.time.Clock;
import java.time.ZoneId;
import java.util.concurrent.TimeUnit;

@EnableAsync
@Configuration
public class BojiaApplicationConfiguration {

    @Bean
    public RetryTemplate retryTemplate() {
        var retryTemplate = new RetryTemplate();

        var exponentialBackOffPolicy = new ExponentialBackOffPolicy();
        exponentialBackOffPolicy.setInitialInterval(TimeUnit.SECONDS.toMillis(2));
        exponentialBackOffPolicy.setMultiplier(5);
        exponentialBackOffPolicy.setMaxInterval(TimeUnit.MINUTES.toMillis(1));
        retryTemplate.setBackOffPolicy(exponentialBackOffPolicy);

        var simpleRetryPolicy = new SimpleRetryPolicy();
        simpleRetryPolicy.setMaxAttempts(3);
        retryTemplate.setRetryPolicy(simpleRetryPolicy);

        return retryTemplate;
    }

    @Bean
    public Clock clock() {
        return Clock.system(ZoneId.of("Europe/Kyiv"));
    }

}
