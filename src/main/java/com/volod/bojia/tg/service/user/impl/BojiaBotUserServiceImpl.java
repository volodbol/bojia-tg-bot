package com.volod.bojia.tg.service.user.impl;

import com.pengrad.telegrambot.model.Update;
import com.volod.bojia.tg.entity.BojiaBotUser;
import com.volod.bojia.tg.repository.BojiaBotUserRepository;
import com.volod.bojia.tg.service.user.BojiaBotUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BojiaBotUserServiceImpl implements BojiaBotUserService {

    private final BojiaBotUserRepository botUserRepository;

    @Override
    public BojiaBotUser getOrCreateUser(Update update) {
        var user = update.message().from();
        return this.botUserRepository.findById(user.id())
                .orElseGet(() -> this.botUserRepository.save(new BojiaBotUser(update)));
    }

    @Override
    public BojiaBotUser save(BojiaBotUser bojiaBotUser) {
        return this.botUserRepository.save(bojiaBotUser);
    }

}
