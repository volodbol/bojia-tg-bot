package com.volod.bojia.tg.service.bot;

import com.pengrad.telegrambot.model.Update;

public interface BojiaBotCommandService {
    Integer processUpdate(Update update);
}
