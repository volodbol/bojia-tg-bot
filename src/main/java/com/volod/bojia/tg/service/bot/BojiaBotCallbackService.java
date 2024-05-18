package com.volod.bojia.tg.service.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.volod.bojia.tg.domain.bot.MessageMarkdownV2;
import com.volod.bojia.tg.service.exception.BojiaExceptionHandlerService;

import java.util.Map;
import java.util.function.Consumer;

import static com.volod.bojia.tg.domain.bot.BojiaBotCallback.SEARCH_REMOVED;
import static com.volod.bojia.tg.domain.bot.BojiaBotCallback.SEARCH_SAVED;
import static com.volod.bojia.tg.domain.bot.BojiaBotCommand.HELP;
import static java.util.Objects.nonNull;

public abstract class BojiaBotCallbackService implements BojiaBotUpdateService {

    protected final TelegramBot bot;
    protected final BojiaExceptionHandlerService exceptionHandlerService;

    private final Map<String, Consumer<Update>> callbackMappings = Map.of(
            SEARCH_SAVED.getValue(), this::processSearchSavedCallback,
            SEARCH_REMOVED.getValue(), this::processSearchRemovedCallback
    );

    protected BojiaBotCallbackService(
            TelegramBot bot,
            BojiaExceptionHandlerService exceptionHandlerService
    ) {
        this.bot = bot;
        this.exceptionHandlerService = exceptionHandlerService;
    }

    public final Integer processUpdate(Update update) {
        try {
            var callbackQuery = update.callbackQuery();
            var command = callbackQuery.data().split("_")[0];
            this.bot.execute(new AnswerCallbackQuery(callbackQuery.id()));
            this.callbackMappings.getOrDefault(command, this::processUnknownCallback).accept(update);
            return update.updateId();
        } catch (RuntimeException ex) {
            this.exceptionHandlerService.publishException(ex);
            this.processUnknownCallback(update);
            return update.updateId();
        }
    }

    public final boolean isUpdateValid(Update update) {
        var callbackQuery = update.callbackQuery();
        return nonNull(callbackQuery)
                && nonNull(callbackQuery.maybeInaccessibleMessage())
                && nonNull(callbackQuery.data());
    }

    public abstract void processSearchSavedCallback(Update update);
    public abstract void processSearchRemovedCallback(Update update);

    public void processUnknownCallback(Update update) {
        this.bot.execute(
                MessageMarkdownV2.builder()
                        .chatId(update)
                        .text("Unknown command received. Send %s to see available commands".formatted(HELP.getValue()))
                        .build()
                        .toSendMessage()
        );
    }

}
