package com.volod.bojia.tg.service.exception.impl;

import com.google.common.base.Throwables;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendDocument;
import com.pengrad.telegrambot.response.BaseResponse;
import com.volod.bojia.tg.constant.BojiaLogConstants;
import com.volod.bojia.tg.property.BojiaApplicationProperties;
import com.volod.bojia.tg.service.exception.BojiaExceptionHandlerService;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ISO_INSTANT;

@Slf4j
@Service
@RequiredArgsConstructor
public class BojiaExceptionHandlerServiceImpl implements BojiaExceptionHandlerService {

    private static final DateTimeFormatter DTF = ISO_INSTANT.withZone(ZoneId.of("Europe/Kyiv"));

    private final TelegramBot bot;
    private final BojiaApplicationProperties applicationProperties;

    @Override
    public void publishException(Throwable ex) {
        LOGGER.debug(BojiaLogConstants.APPLICATION_PREFIX + "exception occurred", ex);
        for (var chatId : this.applicationProperties.getAdminChatIds()) {
            try {
                var response = this.bot.execute(
                        new SendDocument(
                                chatId,
                                "Exception occurred: %s".formatted(Throwables.getStackTraceAsString(ex)).getBytes()
                        ).fileName("Incident-" + DTF.format(Instant.now()) + ".txt")
                );
                if (!response.isOk()) {
                    this.logExceptions(ex, response, null);
                }
            } catch (RuntimeException rex) {
                this.logExceptions(ex, null, rex);
            }
        }
    }

    private void logExceptions(
            Throwable ex,
            @Nullable BaseResponse response,
            @Nullable RuntimeException rex
    ) {
        LOGGER.error(BojiaLogConstants.APPLICATION_PREFIX + "exception occurred", ex);
        LOGGER.error(BojiaLogConstants.BOT_PREFIX + "can't send message with exception", response, rex);
    }

}
