package com.volod.bojia.tg.bot.listener;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.volod.bojia.tg.service.bot.BojiaBotUpdateService;
import com.volod.bojia.tg.service.exception.BojiaExceptionHandlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class BojiaBotUpdatesListener implements UpdatesListener {

    private final List<BojiaBotUpdateService> updateServices;
    private final BojiaExceptionHandlerService exceptionHandlerService;

    @Override
    public int process(List<Update> updates) {
        try {
            LOGGER.trace("Updates - {}", updates);
            return updates.stream()
                    .map(update -> this.updateServices.stream()
                            .filter(service -> service.isUpdateValid(update))
                            .findFirst()
                            .map(service -> service.processUpdate(update))
                            .orElse(CONFIRMED_UPDATES_ALL)
                    )
                    .max(Integer::compareTo)
                    .orElse(CONFIRMED_UPDATES_ALL);
        } catch (RuntimeException ex) {
            this.exceptionHandlerService.publishException(ex);
            return CONFIRMED_UPDATES_ALL;
        }
    }

}
