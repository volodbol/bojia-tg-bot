package com.volod.bojia.tg.service.bot.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.volod.bojia.tg.service.bot.BojiaBotCommandService;
import com.volod.bojia.tg.service.exception.BojiaExceptionHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BojiaBotCommandServiceImpl extends BojiaBotCommandService {

    @Autowired
    public BojiaBotCommandServiceImpl(
            TelegramBot bot,
            BojiaExceptionHandlerService exceptionHandlerService
    ) {
        super(
                bot,
                exceptionHandlerService
        );
    }

    @Override
    public void processHelpCommand(Update update) {
        this.bot.execute(
                new SendMessage(
                        update.message().chat().id(),
                        "Help command received"
                )
        );
    }

    @Override
    public void processUnknownCommand(Update update) {
        this.bot.execute(
                new SendMessage(
                        update.message().chat().id(),
                        "Unknown command received\\. Send `/help` to see available commands!`"
                ).parseMode(ParseMode.MarkdownV2)
        );
    }

}
