package com.volod.bojia.tg.service.shutdown;

import com.pengrad.telegrambot.TelegramBot;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class BojiaShutdownService {

    private final TelegramBot bojiaTelegramBot;

    @PreDestroy
    public void onShutdown() throws InterruptedException {
        this.bojiaTelegramBot.shutdown();
        LOGGER.info("BojIA telegram bot - shutdown initiated");
        TimeUnit.SECONDS.sleep(5);
    }

}
