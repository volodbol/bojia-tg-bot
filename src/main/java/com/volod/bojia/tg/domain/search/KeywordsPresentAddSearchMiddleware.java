package com.volod.bojia.tg.domain.search;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.volod.bojia.tg.domain.bot.MessageMarkdownV2;
import lombok.RequiredArgsConstructor;

import static com.volod.bojia.tg.domain.bot.BojiaBotMyCommand.ADD_PROMPT;
import static com.volod.bojia.tg.domain.bot.BojiaBotMyCommand.DJINNI;

@RequiredArgsConstructor
public class KeywordsPresentAddSearchMiddleware extends AddSearchMiddleware {

    private final TelegramBot bot;

    @Override
    public boolean check(Update update) {
        var keywords = update.message().text().substring(DJINNI.getCommand().length()).trim();
        if (keywords.isBlank()) {
            this.bot.execute(
                    MessageMarkdownV2.builder()
                            .chatId(update.message().chat().id())
                            .text("To add a new search use ")
                            .inlineCode("%s middle java".formatted(DJINNI.getCommand()))
                            .text("\n\nThen we will send you new vacancies with generated cover letter ")
                            .text("according to your %s".formatted(ADD_PROMPT.getCommand()))
                            .build()
                            .toSendMessage()
            );
            return false;
        }
        return this.checkNext(update);
    }

}
