package com.volod.bojia.tg.service.exception.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendDocument;
import com.volod.bojia.tg.constant.BojiaLogConstants;
import com.volod.bojia.tg.property.BojiaApplicationProperties;
import com.volod.bojia.tg.service.exception.BojiaExceptionHandlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static java.time.format.DateTimeFormatter.ISO_INSTANT;

@Slf4j
@Service
@RequiredArgsConstructor
public class BojiaExceptionHandlerServiceImpl implements BojiaExceptionHandlerService {

    private final TelegramBot bot;
    private final BojiaApplicationProperties applicationProperties;

    @Override
    public void publishException(Throwable ex) {
        LOGGER.debug(BojiaLogConstants.APPLICATION_PREFIX + "exception occurred", ex);
        for (var chatId : this.applicationProperties.getAdminChatIds()) {
            try {
                this.bot.execute(
                        new SendDocument(chatId, "Exception occurred: [%s]".formatted(ex))
                                .fileName(LocalDateTime.now().format(ISO_INSTANT) + ".txt")
                );
            } catch (RuntimeException rex) {
                LOGGER.error(BojiaLogConstants.BOT_PREFIX + "can't send message with exception", rex);
                if (!LOGGER.isDebugEnabled()) {
                    LOGGER.error(BojiaLogConstants.APPLICATION_PREFIX + "exception related to message", ex);
                }
            }
        }
    }

}
