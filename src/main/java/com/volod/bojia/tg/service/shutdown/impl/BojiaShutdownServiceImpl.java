package com.volod.bojia.tg.service.shutdown.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.volod.bojia.tg.constant.BojiaLogConstants;
import com.volod.bojia.tg.service.shutdown.BojiaShutdownService;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BojiaShutdownServiceImpl implements BojiaShutdownService {

    private final TelegramBot bojiaTelegramBot;

    @PreDestroy
    @Override
    public void onShutdown() {
        this.bojiaTelegramBot.shutdown();
        LOGGER.info(BojiaLogConstants.BOT_PREFIX + "shutdown initiated");
    }

}
