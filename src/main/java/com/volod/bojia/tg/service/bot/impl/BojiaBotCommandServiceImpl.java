package com.volod.bojia.tg.service.bot.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.response.SendResponse;
import com.volod.bojia.tg.domain.bot.BojiaBotCommand;
import com.volod.bojia.tg.domain.bot.MessageMarkdownV2;
import com.volod.bojia.tg.domain.search.AddSearchMiddleware;
import com.volod.bojia.tg.domain.search.KeywordsPresentAddSearchMiddleware;
import com.volod.bojia.tg.domain.search.PromptAddedAddSearchMiddleware;
import com.volod.bojia.tg.domain.vacancy.VacancyProvider;
import com.volod.bojia.tg.entity.BojiaBotUser;
import com.volod.bojia.tg.entity.BojiaBotUserSearch;
import com.volod.bojia.tg.service.bot.BojiaBotCommandService;
import com.volod.bojia.tg.service.bot.BojiaBotUserSearchService;
import com.volod.bojia.tg.service.bot.BojiaBotUserService;
import com.volod.bojia.tg.service.exception.BojiaExceptionHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.volod.bojia.tg.domain.bot.BojiaBotCommand.*;

@Slf4j
@Service
public class BojiaBotCommandServiceImpl extends BojiaBotCommandService {

    // Services
    private final BojiaBotUserService botUserService;
    private final BojiaBotUserSearchService botUserSearchService;
    // Middleware
    private final AddSearchMiddleware addSearchMiddleware;

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
        this.addSearchMiddleware = AddSearchMiddleware.link(
                new PromptAddedAddSearchMiddleware(bot, botUserService),
                List.of(
                        new KeywordsPresentAddSearchMiddleware(bot)
                )
        );
    }

    @Override
    public void processHelpCommand(Update update) {
        var sendResponse = this.bot.execute(
                MessageMarkdownV2.builder()
                        .chatId(update)
                        .text("Bojia is a bot that can monitor your favorite job websites ")
                        .text("and notify you that a new vacancy is posted helping you answer faster.")
                        .text("\n\nWe will ask you to add a prompt about your experience to generate a cover letter ")
                        .text("using AI which you can edit and send with your CV.")
                        .bold("\n\nAvailable commands:")
                        .text("\n" + BojiaBotCommand.getJoinedCommands())
                        .build()
                        .toSendMessage()
        );
        this.logResponse("processHelpCommand", sendResponse);
    }

    @Override
    public void processAddPromptCommand(Update update) {
        var message = update.message();
        var prompt = message.text().substring(ADD_PROMPT.getValue().length()).trim();
        SendResponse sendResponse;
        if (prompt.isBlank()) {
            var user = this.botUserService.getOrCreateUser(update);
            sendResponse = this.bot.execute(
                    MessageMarkdownV2.builder()
                            .chatId(update)
                            .text("To generate cover letter we have to know something about you.")
                            .text("\n\nSend")
                            .inlineCode("%n%s ".formatted(ADD_PROMPT.getValue()) +
                                    "I'm Java Software Engineer with 4+ years of experience." +
                                    "Worked with Java 17, SpringBoot 3, CI/CD, Kafka, AWS")
                            .text("\nto save a new prompt.")
                            .bold("\n\nCurrent prompt: ").text(user.getPromptOrDefault())
                            .build()
                            .toSendMessage()
            );
        } else {
            var user = this.botUserService.save(new BojiaBotUser(update, prompt));
            sendResponse = this.bot.execute(
                    MessageMarkdownV2.builder()
                            .chatId(update)
                            .text("Prompt successfully saved: ")
                            .inlineCode(user.getPromptOrDefault())
                            .build()
                            .toSendMessage()
            );
        }
        this.logResponse("processAddPromptCommand", sendResponse);
    }

    @Override
    public void processSearchesCommand(Update update) {
        var searches = this.botUserSearchService.getByUserId(update.message().from().id());
        var sendResponse = this.bot.execute(
                MessageMarkdownV2.builder()
                        .chatId(update)
                        .text("%s".formatted(searches.getKeywordsOrDefault()))
                        .text("\n\nTo remove search send remove command and search id: ")
                        .inlineCode("%s 432345".formatted(REMOVE_SEARCH.getValue()))
                        .build()
                        .toSendMessage()
        );
        this.logResponse("processSearchesCommand", sendResponse);
    }

    @Override
    public void processAddSearchCommand(Update update, VacancyProvider provider) {
        var message = update.message();
        var user = this.botUserService.getOrCreateUser(update);
        var botCommand = provider.getBotCommand();
        var keywords = message.text().substring(botCommand.getValue().length()).trim();
        if (this.addSearchMiddleware.check(update, botCommand)) {
            var search = this.botUserSearchService.save(new BojiaBotUserSearch(user, provider, keywords));
            var sendResponse = this.bot.execute(
                    MessageMarkdownV2.builder()
                            .chatId(update)
                            .text("Search successfully saved!")
                            .text("%n%n%s - %s".formatted(search.getId(), search.getKeywords()))
                            .build()
                            .toSendMessage()
            );
            this.logResponse("processAddSearchCommand", sendResponse);
        }
    }

    @Override
    public void processRemoveSearchCommand(Update update) {
        try {
            var message = update.message();
            var searchId = Long.parseLong(message.text().substring(REMOVE_SEARCH.getValue().length()).trim());
            this.botUserSearchService.delete(message.from().id(), searchId);
            var sendResponse = this.bot.execute(
                    MessageMarkdownV2.builder()
                            .chatId(update)
                            .text("Search with id: %s successfully deleted".formatted(searchId))
                            .build()
                            .toSendMessage()
            );
            this.logResponse("processRemoveSearchCommand", sendResponse);
        } catch (NumberFormatException ex) {
            var sendResponse = this.bot.execute(
                    MessageMarkdownV2.builder()
                            .chatId(update)
                            .text("Can't delete search. Make sure that you sent correct command with proper id value: ")
                            .inlineCode("%s 432345".formatted(REMOVE_SEARCH.getValue()))
                            .build()
                            .toSendMessage()
            );
            this.logResponse("processRemoveSearchCommand", sendResponse);
        }
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
