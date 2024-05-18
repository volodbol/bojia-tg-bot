package com.volod.bojia.tg.service.bot.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.volod.bojia.tg.service.bot.BojiaBotCallbackService;
import com.volod.bojia.tg.service.exception.BojiaExceptionHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class BojiaBotCallbackServiceImpl extends BojiaBotCallbackService {

    @Autowired
    public BojiaBotCallbackServiceImpl(
            TelegramBot bot,
            BojiaExceptionHandlerService exceptionHandlerService
    ) {
        super(
                bot,
                exceptionHandlerService
        );
    }

}
