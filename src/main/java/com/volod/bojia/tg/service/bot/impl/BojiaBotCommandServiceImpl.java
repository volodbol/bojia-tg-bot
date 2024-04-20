package com.volod.bojia.tg.service.bot.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.volod.bojia.tg.service.bot.BojiaBotCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BojiaBotCommandServiceImpl implements BojiaBotCommandService {

    private final TelegramBot bot;

    @Override
    public Integer processUpdate(Update update) {
        this.bot.execute(
                new SendMessage(
                        update.message().chat().id(),
                        "Message received"
                )
        );
        return update.updateId();
    }

}
