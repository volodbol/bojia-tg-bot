package com.volod.bojia.tg.bot.listener;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.volod.bojia.tg.service.bot.BojiaBotUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class BojiaBotUpdatesListener implements UpdatesListener {

    private final BojiaBotUserService botUserService;

    @Override
    public int process(List<Update> updates) {
        LOGGER.info("Updates - {}", updates);
        updates.forEach(this.botUserService::extractAndSaveUser);
        return CONFIRMED_UPDATES_ALL;
    }

}
