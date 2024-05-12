package com.volod.bojia.tg.service.bot.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import com.volod.bojia.tg.domain.bot.BojiaBotMyCommand;
import com.volod.bojia.tg.domain.bot.MessageMarkdownV2;
import com.volod.bojia.tg.entity.BojiaBotUser;
import com.volod.bojia.tg.service.bot.BojiaBotCommandService;
import com.volod.bojia.tg.service.bot.BojiaBotUserSearchService;
import com.volod.bojia.tg.service.bot.BojiaBotUserService;
import com.volod.bojia.tg.service.exception.BojiaExceptionHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.volod.bojia.tg.domain.bot.BojiaBotMyCommand.ADD_PROMPT;
import static com.volod.bojia.tg.domain.bot.BojiaBotMyCommand.REMOVE_SEARCH;

@Slf4j
@Service
public class BojiaBotCommandServiceImpl extends BojiaBotCommandService {

    private final BojiaBotUserService botUserService;
    private final BojiaBotUserSearchService botUserSearchService;

    @Autowired
    public BojiaBotCommandServiceImpl(
            TelegramBot bot,
            BojiaExceptionHandlerService exceptionHandlerService,
            BojiaBotUserService botUserService,
            BojiaBotUserSearchService botUserSearchService
    ) {
        super(
                bot,
                exceptionHandlerService
        );
        this.botUserService = botUserService;
        this.botUserSearchService = botUserSearchService;
    }

    @Override
    public void processHelpCommand(Update update) {
        var sendResponse = this.bot.execute(
                MessageMarkdownV2.builder()
                        .chatId(update.message().chat().id())
                        .text("Bojia is a bot that can monitor your favorite job websites ")
                        .text("and notify you that a new vacancy is posted helping you answer faster.")
                        .text("\n\nWe will ask you to add a prompt about your experience to generate a cover letter ")
                        .text("using AI which you can edit and send with your CV.")
                        .bold("\n\nAvailable commands:")
                        .text("\n" + BojiaBotMyCommand.getJoinedCommands())
                        .build()
                        .toSendMessage()
        );
        this.logResponse("processHelpCommand", sendResponse);
    }

    @Override
    public void processAddPromptCommand(Update update) {
        var message = update.message();
        var prompt = message.text().substring(ADD_PROMPT.getCommand().length());
        SendResponse sendResponse;
        if (prompt.isBlank()) {
            var user = this.botUserService.getOrCreateUser(update);
            sendResponse = this.bot.execute(
                    MessageMarkdownV2.builder()
                            .chatId(update.message().chat().id())
                            .text("To generate cover letter we have to know something about you.")
                            .text("\n\nSend")
                            .inlineCode("\n/prompt " +
                                    "I'm Java Software Engineer with 4+ years of experience." +
                                    "Worked with Java 17, SpringBoot 3, CI/CD, Kafka, AWS")
                            .text("\nto save a new prompt.")
                            .bold("\n\nCurrent prompt: ").text(user.getPromptOrDefault())
                            .build()
                            .toSendMessage()
            );
        } else {
            this.botUserService.save(new BojiaBotUser(update, prompt));
            sendResponse = this.bot.execute(
                    new SendMessage(
                            update.message().chat().id(),
                            "Prompt successfully saved"
                    )
            );
        }
        this.logResponse("processAddPromptCommand", sendResponse);
    }

    @Override
    public void processSearchesCommand(Update update) {
        var searches = this.botUserSearchService.getByUserId(update.message().from().id());
        var sendResponse = this.bot.execute(
                MessageMarkdownV2.builder()
                        .chatId(update.message().chat().id())
                        .text("\n\nTo remove search send remove command and search id: ")
                        .inlineCode("/remove 432345")
                        .text("%n%n%s".formatted(searches.getKeywordsOrDefault()))
                        .build()
                        .toSendMessage()
        );
        this.logResponse("processSearchesCommand", sendResponse);
    }

    @Override
    public void processRemoveSearchCommand(Update update) {
        try {
            var message = update.message();
            var searchId = Long.parseLong(message.text().substring(REMOVE_SEARCH.getCommand().length()).trim());
            this.botUserSearchService.delete(message.from().id(), searchId);
            var sendResponse = this.bot.execute(
                    MessageMarkdownV2.builder()
                            .chatId(update.message().chat().id())
                            .text("Search with id: %s successfully deleted".formatted(searchId))
                            .build()
                            .toSendMessage()
            );
            this.logResponse("processRemoveSearchCommand", sendResponse);
        } catch (NumberFormatException ex) {
            var sendResponse = this.bot.execute(
                    MessageMarkdownV2.builder()
                            .chatId(update.message().chat().id())
                            .text("Can't delete search. Make sure that you sent correct command with proper id value: ")
                            .inlineCode("/remove 432345")
                            .build()
                            .toSendMessage()
            );
            this.logResponse("processRemoveSearchCommand", sendResponse);
        }
    }

    @Override
    public void processUnknownCommand(Update update) {
        var sendResponse = this.bot.execute(
                MessageMarkdownV2.builder()
                        .chatId(update.message().chat().id())
                        .text("Unknown command received. Send /help to see available commands")
                        .build()
                        .toSendMessage()
        );
        this.logResponse("processUnknownCommand", sendResponse);
    }

    // -----------------------------------------------------------------------------------------------------------------
    // PRIVATE METHODS
    // -----------------------------------------------------------------------------------------------------------------
    private void logResponse(String methodName, SendResponse response) {
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("[{}] send message response: {}", methodName, response);
        }
    }
}
