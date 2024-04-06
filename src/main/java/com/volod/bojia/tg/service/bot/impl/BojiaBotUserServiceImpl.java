package com.volod.bojia.tg.service.bot.impl;

import com.pengrad.telegrambot.model.Update;
import com.volod.bojia.tg.entity.BojiaBotUser;
import com.volod.bojia.tg.repository.BojiaBotUserRepository;
import com.volod.bojia.tg.service.bot.BojiaBotUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BojiaBotUserServiceImpl implements BojiaBotUserService {

    private final BojiaBotUserRepository botUserRepository;

    @Override
    public BojiaBotUser save(BojiaBotUser bojiaBotUser) {
        return botUserRepository.save(bojiaBotUser);
    }

    @Override
    public BojiaBotUser extractAndSaveUser(Update update) {
        var user = update.message().from();
        var chat = update.message().chat();
        return this.botUserRepository.save(new BojiaBotUser(user, chat));
    }

}
