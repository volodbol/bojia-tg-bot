package com.volod.bojia.tg.service.bot;

import com.pengrad.telegrambot.model.Update;

public interface BojiaBotUpdateService {
    boolean isUpdateValid(Update update);
    Integer processUpdate(Update update);
}
