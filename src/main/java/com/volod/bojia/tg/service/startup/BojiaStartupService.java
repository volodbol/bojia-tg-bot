package com.volod.bojia.tg.service.startup;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public interface BojiaStartupService extends ApplicationListener<ContextRefreshedEvent> {

    @Override
    default void onApplicationEvent(@NotNull ContextRefreshedEvent event) {
        this.initializeBot();
    }

    void initializeBot();

}
