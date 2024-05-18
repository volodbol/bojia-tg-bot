package com.volod.bojia.tg.domain.search;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.volod.bojia.tg.domain.bot.MessageMarkdownV2;
import com.volod.bojia.tg.domain.vacancy.VacancyProvider;
import lombok.RequiredArgsConstructor;

import static com.volod.bojia.tg.domain.bot.BojiaBotCommand.ADD_PROMPT;

@RequiredArgsConstructor
public class KeywordsPresentAddSearchMiddleware extends AddSearchMiddleware {

    private final TelegramBot bot;

    @Override
    public boolean check(Update update, VacancyProvider provider) {
        var command = provider.getBotCommand();
        var keywords = update.message().text().substring(command.getValue().length()).trim();
        if (keywords.isBlank()) {
            this.bot.execute(
                    MessageMarkdownV2.builder()
                            .chatId(update.message().chat().id())
                            .text("To add a new search use ")
                            .inlineCode("%s middle java".formatted(command.getValue()))
                            .text("\n\nThen we will be sending you new vacancies with generated cover letter ")
                            .text("according to your %s".formatted(ADD_PROMPT.getValue()))
                            .build()
                            .toSendMessage()
            );
            return false;
        }
        return this.checkNext(update, provider);
    }

}
