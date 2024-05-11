package com.volod.bojia.tg.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class BojiaApplicationConfiguration {

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

}
