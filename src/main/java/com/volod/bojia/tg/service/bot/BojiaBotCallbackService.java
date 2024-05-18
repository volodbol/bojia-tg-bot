package com.volod.bojia.tg.service.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.volod.bojia.tg.domain.bot.MessageMarkdownV2;
import com.volod.bojia.tg.domain.exception.BojiaBotUpdateIllegal;
import com.volod.bojia.tg.service.exception.BojiaExceptionHandlerService;

import java.util.Map;
import java.util.function.Consumer;

import static com.volod.bojia.tg.domain.bot.BojiaBotCommand.HELP;
import static java.util.Objects.isNull;

public abstract class BojiaBotCallbackService {

    protected final TelegramBot bot;
    protected final BojiaExceptionHandlerService exceptionHandlerService;

    private final Map<String, Consumer<Update>> callbackMappings = Map.of(
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
            this.validateUpdate(update);
            var message = update.message();
            var command = message.text().split(" ")[0];
            this.callbackMappings.getOrDefault(command, this::processUnknownCallback).accept(update);
            return update.updateId();
        } catch (BojiaBotUpdateIllegal | RuntimeException ex) {
            this.exceptionHandlerService.publishException(ex);
        }
        this.processUnknownCallback(update);
        return update.updateId();
    }

    public final void validateUpdate(Update update) throws BojiaBotUpdateIllegal {
        var callbackQuery = update.callbackQuery();
        if (isNull(callbackQuery) || isNull(callbackQuery.data())) {
            throw new BojiaBotUpdateIllegal(update);
        }
    }

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
