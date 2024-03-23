package com.volod.bojia.tg.bot;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class BojiaBotUpdatesListener implements UpdatesListener {

    @Override
    public int process(List<Update> updates) {
        LOGGER.info("Updates - {}", updates);
        return CONFIRMED_UPDATES_ALL;
    }

}
