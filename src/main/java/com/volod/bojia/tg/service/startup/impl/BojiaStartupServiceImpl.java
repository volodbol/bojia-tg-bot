package com.volod.bojia.tg.service.startup.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.volod.bojia.tg.bot.handler.BojiaBotExceptionHandler;
import com.volod.bojia.tg.bot.listener.BojiaBotUpdatesListener;
import com.volod.bojia.tg.constant.BojiaLogConstants;
import com.volod.bojia.tg.domain.bot.BojiaBotMyCommand;
import com.volod.bojia.tg.service.bot.BojiaBotDetailsService;
import com.volod.bojia.tg.service.startup.BojiaStartupService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BojiaStartupServiceImpl implements BojiaStartupService {

    // Bot
    private final TelegramBot bojiaBot;
    private final BojiaBotUpdatesListener bojiaBotUpdatesListener;
    private final BojiaBotExceptionHandler bojiaBotExceptionHandler;
    // Service
    private final BojiaBotDetailsService bojiaBotDetailsService;

    @Override
    public void initializeBot() {
        LOGGER.info(BojiaLogConstants.BOT_PREFIX + "initialization started");
        var botDetails = this.bojiaBotDetailsService.findOneOrCreate();
        if (!botDetails.isMyCommandsPresent()) {
            var setMyCommandsResponse = this.bojiaBot.execute(new SetMyCommands(BojiaBotMyCommand.getBotCommands()));
            LOGGER.info(BojiaLogConstants.BOT_PREFIX + "set my commands response: {}", setMyCommandsResponse);
            botDetails.setMyCommandsPresent(setMyCommandsResponse.isOk());
        }
        this.bojiaBotDetailsService.save(botDetails);
        this.bojiaBot.setUpdatesListener(this.bojiaBotUpdatesListener, this.bojiaBotExceptionHandler);
        LOGGER.info(BojiaLogConstants.BOT_PREFIX + "initialization completed");
    }

}
