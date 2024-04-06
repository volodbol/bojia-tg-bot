package com.volod.bojia.tg.service.exception.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendDocument;
import com.volod.bojia.tg.property.BojiaApplicationProperties;
import com.volod.bojia.tg.service.exception.BojiaExceptionHandlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class BojiaExceptionHandlerServiceImpl implements BojiaExceptionHandlerService {

    private final TelegramBot bojiaBot;
    private final BojiaApplicationProperties applicationProperties;

    @Override
    public void publishException(Throwable ex) {
        for (var chatId : this.applicationProperties.getAdminChatIds()) {
            try {
                this.bojiaBot.execute(
                        new SendDocument(
                                chatId,
                                "Exception occurred: [%s]".formatted(ex)
                        ).fileName(LocalDateTime.now().format(DateTimeFormatter.ISO_INSTANT) + ".txt")
                );
            } catch (RuntimeException rex) {
                LOGGER.error("Can't send message with exception:", rex);
            }
        }
    }

}
