package com.volod.bojia.tg.service.user;

import com.pengrad.telegrambot.model.Update;
import com.volod.bojia.tg.entity.BojiaBotUser;

public interface BojiaBotUserService {
    BojiaBotUser getOrCreateUser(Update update);
    BojiaBotUser save(BojiaBotUser bojiaBotUser);
}
