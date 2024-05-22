package com.volod.bojia.tg.service.bot.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.volod.bojia.tg.domain.bot.BojiaBotCallback;
import com.volod.bojia.tg.domain.bot.BojiaBotCommand;
import com.volod.bojia.tg.domain.bot.MessageMarkdownV2;
import com.volod.bojia.tg.domain.search.AddSearchMiddleware;
import com.volod.bojia.tg.domain.search.KeywordsPresentAddSearchMiddleware;
import com.volod.bojia.tg.domain.search.PromptAddedAddSearchMiddleware;
import com.volod.bojia.tg.domain.search.SearchUniqueAddSearchMiddleware;
import com.volod.bojia.tg.domain.vacancy.VacancyProvider;
import com.volod.bojia.tg.entity.BojiaBotUser;
import com.volod.bojia.tg.entity.BojiaBotUserSearch;
import com.volod.bojia.tg.service.bot.BojiaBotCommandService;
import com.volod.bojia.tg.service.search.BojiaBotUserSearchService;
import com.volod.bojia.tg.service.user.BojiaBotUserService;
import com.volod.bojia.tg.service.exception.BojiaExceptionHandlerService;
import com.volod.bojia.tg.service.vacancy.VacancyProvidersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.volod.bojia.tg.domain.bot.BojiaBotCallback.SEARCH_REMOVED;
import static com.volod.bojia.tg.domain.bot.BojiaBotCallback.SEARCH_SAVED;
import static com.volod.bojia.tg.domain.bot.BojiaBotCommand.*;

@Service
public class BojiaBotCommandServiceImpl extends BojiaBotCommandService {

    // Services
    private final BojiaBotUserService botUserService;
    private final BojiaBotUserSearchService botUserSearchService;
    private final VacancyProvidersService vacancyProvidersService;
    // Middleware
    private final AddSearchMiddleware addSearchMiddleware;

    @Autowired
    public BojiaBotCommandServiceImpl(
            TelegramBot bot,
            BojiaExceptionHandlerService exceptionHandlerService,
            BojiaBotUserService botUserService,
            BojiaBotUserSearchService botUserSearchService,
            VacancyProvidersService vacancyProvidersService
    ) {
        super(
                bot,
                exceptionHandlerService
        );
        this.botUserService = botUserService;
        this.botUserSearchService = botUserSearchService;
        this.vacancyProvidersService = vacancyProvidersService;
        this.addSearchMiddleware = AddSearchMiddleware.link(
                new PromptAddedAddSearchMiddleware(bot, botUserService),
                List.of(
                        new KeywordsPresentAddSearchMiddleware(bot),
                        new SearchUniqueAddSearchMiddleware(bot, botUserSearchService)
                )
        );
    }

    @Override
    public void processHelpCommand(Update update) {
        this.bot.execute(
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
    }

    @Override
    public void processAddPromptCommand(Update update) {
        var message = update.message();
        var prompt = message.text().substring(ADD_PROMPT.getValue().length()).trim();
        if (prompt.isBlank()) {
            var user = this.botUserService.getOrCreateUser(update);
            this.bot.execute(
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
            this.bot.execute(
                    MessageMarkdownV2.builder()
                            .chatId(update)
                            .text("Prompt successfully saved: ")
                            .inlineCode(user.getPromptOrDefault())
                            .build()
                            .toSendMessage()
            );
        }
    }

    @Override
    public void processSearchesCommand(Update update) {
        var searches = this.botUserSearchService.getByUserId(update.message().from().id());
        this.bot.execute(
                MessageMarkdownV2.builder()
                        .chatId(update)
                        .text("%s".formatted(searches.getKeywordsOrDefault()))
                        .text("\n\nTo remove search send remove command and search id: ")
                        .inlineCode("%s 432345".formatted(REMOVE_SEARCH.getValue()))
                        .build()
                        .toSendMessage()
        );
    }

    @Override
    public void processAddSearchCommand(Update update, VacancyProvider provider) {
        var message = update.message();
        var user = this.botUserService.getOrCreateUser(update);
        var botCommand = provider.getBotCommand();
        var keywords = message.text().substring(botCommand.getValue().length()).trim();
        if (this.addSearchMiddleware.check(update, provider)) {
            var search = this.botUserSearchService.save(new BojiaBotUserSearch(user, provider, keywords));
            var sendResponse = this.sendPleaseWaitMessage(update, "Fetching vacancies");
            var numberOfVacancies = this.vacancyProvidersService.getNumberOfVacancies(provider, search.getKeywordsSplit());
            this.bot.execute(
                    MessageMarkdownV2.builder()
                            .chatId(update)
                            .messageId(sendResponse.message().messageId())
                            .text("Found %d vacancies for:".formatted(numberOfVacancies))
                            .text("%n%n%s - %s".formatted(provider.getReadableName(), search.getKeywords()))
                            .text("\n\nDo you still want to add this search?")
                            .build()
                            .toEditMessageText()
                            .replyMarkup(
                                    new InlineKeyboardMarkup(
                                            new InlineKeyboardButton("Yes")
                                                    .callbackData(
                                                            BojiaBotCallback.data(SEARCH_SAVED, search.getId())
                                                    ),
                                            new InlineKeyboardButton("No")
                                                    .callbackData(
                                                            BojiaBotCallback.data(SEARCH_REMOVED, search.getId())
                                                    )
                                    )
                            )
            );
        }
    }

    @Override
    public void processRemoveSearchCommand(Update update) {
        try {
            var message = update.message();
            var searchId = Long.parseLong(message.text().substring(REMOVE_SEARCH.getValue().length()).trim());
            this.botUserSearchService.delete(message.from().id(), searchId);
            this.bot.execute(
                    MessageMarkdownV2.builder()
                            .chatId(update)
                            .text("Search with id %s successfully deleted".formatted(searchId))
                            .build()
                            .toSendMessage()
            );
        } catch (NumberFormatException ex) {
            this.bot.execute(
                    MessageMarkdownV2.builder()
                            .chatId(update)
                            .text("To delete a search, get search id from %s and send - ".formatted(SEARCHES.getValue()))
                            .inlineCode("%s 432345".formatted(REMOVE_SEARCH.getValue()))
                            .build()
                            .toSendMessage()
            );
        }
    }

}
