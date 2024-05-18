package com.volod.bojia.tg.service.bot.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.volod.bojia.tg.domain.bot.MessageMarkdownV2;
import com.volod.bojia.tg.service.bot.BojiaBotCallbackService;
import com.volod.bojia.tg.service.bot.BojiaBotUserSearchService;
import com.volod.bojia.tg.service.exception.BojiaExceptionHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.volod.bojia.tg.domain.bot.BojiaBotCallback.SEARCH_REMOVED;
import static com.volod.bojia.tg.domain.bot.BojiaBotCommand.SEARCHES;

@Slf4j
@Service
public class BojiaBotCallbackServiceImpl extends BojiaBotCallbackService {

    private final BojiaBotUserSearchService botUserSearchService;

    @Autowired
    public BojiaBotCallbackServiceImpl(
            TelegramBot bot,
            BojiaExceptionHandlerService exceptionHandlerService,
            BojiaBotUserSearchService botUserSearchService
    ) {
        super(
                bot,
                exceptionHandlerService
        );
        this.botUserSearchService = botUserSearchService;
    }

    @Override
    public void processSearchSavedCallback(Update update) {
        this.bot.execute(
                MessageMarkdownV2.builder()
                        .chatId(update)
                        .messageId(update)
                        .text("Search successfully saved! ")
                        .text("To see all searches use - %s".formatted(SEARCHES.getValue()))
                        .build()
                        .toEditMessageText()
        );
    }

    @Override
    public void processSearchRemovedCallback(Update update) {
        var callbackQuery = update.callbackQuery();
        var userId = callbackQuery.from().id();
        var searchId = Long.parseLong(callbackQuery.data().substring(SEARCH_REMOVED.length()));
        this.botUserSearchService.delete(userId, searchId);
        this.bot.execute(
                MessageMarkdownV2.builder()
                        .chatId(update)
                        .messageId(update)
                        .text("Search successfully deleted! ")
                        .text("To see all searches use - %s".formatted(SEARCHES.getValue()))
                        .build()
                        .toEditMessageText()
        );
    }

}
