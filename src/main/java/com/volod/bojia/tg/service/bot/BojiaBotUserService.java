package com.volod.bojia.tg.service.bot;

import com.pengrad.telegrambot.model.Update;
import com.volod.bojia.tg.entity.BojiaBotUser;

public interface BojiaBotUserService {
    BojiaBotUser save(BojiaBotUser bojiaBotUser);
    BojiaBotUser extractAndSaveUser(Update update);
}
