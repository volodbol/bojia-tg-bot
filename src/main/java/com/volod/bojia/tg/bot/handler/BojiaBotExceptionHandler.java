package com.volod.bojia.tg.bot.handler;

import com.pengrad.telegrambot.ExceptionHandler;
import com.pengrad.telegrambot.TelegramException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BojiaBotExceptionHandler implements ExceptionHandler {

    @Override
    public void onException(TelegramException ex) {
        LOGGER.error("Exception occurred", ex);
    }

}
