package com.volod.bojia.tg.bot.handler;

import com.pengrad.telegrambot.ExceptionHandler;
import com.pengrad.telegrambot.TelegramException;
import com.volod.bojia.tg.service.exception.BojiaExceptionHandlerService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BojiaBotExceptionHandler implements ExceptionHandler {

    private final BojiaExceptionHandlerService bojiaExceptionHandlerService;

    @Override
    public void onException(TelegramException ex) {
        this.bojiaExceptionHandlerService.publishException(ex);
    }

}
