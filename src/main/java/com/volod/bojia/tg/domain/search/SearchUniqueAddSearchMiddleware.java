package com.volod.bojia.tg.domain.search;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.volod.bojia.tg.domain.bot.MessageMarkdownV2;
import com.volod.bojia.tg.domain.vacancy.VacancyProvider;
import com.volod.bojia.tg.service.search.BojiaBotUserSearchService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SearchUniqueAddSearchMiddleware extends AddSearchMiddleware {

    private final TelegramBot bot;
    private final BojiaBotUserSearchService botUserSearchService;

    @Override
    public boolean check(Update update, VacancyProvider provider) {
        var message = update.message();
        var userId = message.from().id();
        var command = provider.getBotCommand();
        var keywords = message.text().substring(command.getValue().length()).trim();
        if (this.botUserSearchService.exists(userId, provider, keywords)) {
            this.bot.execute(
                    MessageMarkdownV2.builder()
                            .chatId(update)
                            .text("This search already exist, add another using %s".formatted(command.getValue()))
                            .build()
                            .toSendMessage()
            );
            return false;
        }
        return this.checkNext(update, provider);
    }

}
