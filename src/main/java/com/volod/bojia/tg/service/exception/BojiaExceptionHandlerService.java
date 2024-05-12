package com.volod.bojia.tg.service.exception;

import org.springframework.scheduling.annotation.Async;

public interface BojiaExceptionHandlerService {
    @Async
    void publishException(Throwable ex);
}
