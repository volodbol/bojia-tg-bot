package com.volod.bojia.tg.service.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.response.SendResponse;
import com.volod.bojia.tg.domain.bot.MessageMarkdownV2;
import com.volod.bojia.tg.domain.vacancy.VacancyProvider;
import com.volod.bojia.tg.service.exception.BojiaExceptionHandlerService;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.function.Consumer;

import static com.volod.bojia.tg.domain.bot.BojiaBotCommand.*;
import static java.util.Objects.nonNull;

@Slf4j
public abstract class BojiaBotCommandService implements BojiaBotUpdateService {

    protected final TelegramBot bot;
    protected final BojiaExceptionHandlerService exceptionHandlerService;

    private final Map<String, Consumer<Update>> commandMappings = Map.of(
            HELP.getValue(), this::processHelpCommand,
            ADD_PROMPT.getValue(), this::processAddPromptCommand,
            SEARCHES.getValue(), this::processSearchesCommand,
            DJINNI.getValue(), update -> this.processAddSearchCommand(update, VacancyProvider.DJINNI),
            REMOVE_SEARCH.getValue(), this::processRemoveSearchCommand
    );

    protected BojiaBotCommandService(
            TelegramBot bot,
            BojiaExceptionHandlerService exceptionHandlerService
    ) {
        this.bot = bot;
        this.exceptionHandlerService = exceptionHandlerService;
    }

    public final Integer processUpdate(Update update) {
        try {
            var message = update.message();
            var command = message.text().split(" ")[0];
            this.commandMappings.getOrDefault(command, this::processUnknownCommand).accept(update);
            return update.updateId();
        } catch (RuntimeException ex) {
            this.exceptionHandlerService.publishException(ex);
            this.processUnknownCommand(update);
            return update.updateId();
        }
    }

    public final boolean isUpdateValid(Update update) {
        var message = update.message();
        return nonNull(message) && nonNull(message.text());
    }

    public abstract void processHelpCommand(Update update);
    public abstract void processAddPromptCommand(Update update);
    public abstract void processSearchesCommand(Update update);
    public abstract void processAddSearchCommand(Update update, VacancyProvider provider);
    public abstract void processRemoveSearchCommand(Update update);

    public final SendResponse sendPleaseWaitMessage(Update update, String reason) {
        return this.bot.execute(
                MessageMarkdownV2.builder()
                        .chatId(update)
                        .text("%s. Please wait...".formatted(reason))
                        .build()
                        .toSendMessage()
        );
    }

    public void processUnknownCommand(Update update) {
        this.bot.execute(
                MessageMarkdownV2.builder()
                        .chatId(update)
                        .text("Unknown command received. Send %s to see available commands".formatted(HELP.getValue()))
                        .build()
                        .toSendMessage()
        );
    }

}
