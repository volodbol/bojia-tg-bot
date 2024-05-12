package com.volod.bojia.tg.service.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.volod.bojia.tg.domain.exception.BojiaBotUpdateIllegal;
import com.volod.bojia.tg.service.exception.BojiaExceptionHandlerService;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.function.Consumer;

import static com.volod.bojia.tg.domain.bot.BojiaBotMyCommand.ADD_PROMPT;
import static com.volod.bojia.tg.domain.bot.BojiaBotMyCommand.HELP;
import static java.util.Objects.isNull;

@Slf4j
public abstract class BojiaBotCommandService {

    protected final TelegramBot bot;
    protected final BojiaExceptionHandlerService exceptionHandlerService;

    private final Map<String, Consumer<Update>> commandMappings = Map.of(
            HELP.getCommand(), this::processHelpCommand,
            ADD_PROMPT.getCommand(), this::processAddPromptCommand
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
            this.validateUpdate(update);
            var message = update.message();
            var command = message.text().split(" ")[0];
            this.commandMappings.getOrDefault(command, this::processUnknownCommand).accept(update);
            return update.updateId();
        } catch (BojiaBotUpdateIllegal ex) {
            LOGGER.debug("Can't process update", ex);
        } catch (RuntimeException ex) {
            this.exceptionHandlerService.publishException(ex);
        }
        this.processUnknownCommand(update);
        return update.updateId();
    }

    public final void validateUpdate(Update update) throws BojiaBotUpdateIllegal {
        var message = update.message();
        if (isNull(message) || isNull(message.text())) {
            throw new BojiaBotUpdateIllegal(update);
        }
    }

    public abstract void processHelpCommand(Update update);
    public abstract void processAddPromptCommand(Update update);
    public abstract void processUnknownCommand(Update update);

}
