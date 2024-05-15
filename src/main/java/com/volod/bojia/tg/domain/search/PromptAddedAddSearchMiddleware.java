package com.volod.bojia.tg.domain.search;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.volod.bojia.tg.domain.bot.BojiaBotCommand;
import com.volod.bojia.tg.domain.bot.MessageMarkdownV2;
import com.volod.bojia.tg.service.bot.BojiaBotUserService;
import lombok.RequiredArgsConstructor;

import static com.volod.bojia.tg.domain.bot.BojiaBotCommand.ADD_PROMPT;

@RequiredArgsConstructor
public class PromptAddedAddSearchMiddleware extends AddSearchMiddleware {

    private final TelegramBot bot;
    private final BojiaBotUserService botUserService;

    @Override
    public boolean check(Update update, BojiaBotCommand command) {
        var user = this.botUserService.getOrCreateUser(update);
        if (user.isPromptAbsent()) {
            this.bot.execute(
                    MessageMarkdownV2.builder()
                            .chatId(update.message().chat().id())
                            .text("You can't add a new search. ")
                            .text("Use %s to add prompt first. ".formatted(ADD_PROMPT.getValue()))
                            .text("Then you will be able to add a new search")
                            .build()
                            .toSendMessage()
            );
            return false;
        }
        return this.checkNext(update, command);
    }

}
