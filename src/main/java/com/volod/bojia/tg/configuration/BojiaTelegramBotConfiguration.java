package com.volod.bojia.tg.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SetMyCommands;
import com.volod.bojia.tg.bot.handler.BojiaBotExceptionHandler;
import com.volod.bojia.tg.bot.listener.BojiaBotUpdatesListener;
import com.volod.bojia.tg.constant.BojiaLogPrefixes;
import com.volod.bojia.tg.domain.bot.BojiaBotMyCommand;
import com.volod.bojia.tg.property.BojiaApplicationProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class BojiaTelegramBotConfiguration {

    private final BojiaApplicationProperties applicationProperties;

    @Bean
    public TelegramBot bojiaTelegramBot(
            BojiaBotUpdatesListener updatesListener,
            BojiaBotExceptionHandler exceptionHandler
    ) {
        var bot = new TelegramBot(this.applicationProperties.getBotToken());
        bot.setUpdatesListener(updatesListener, exceptionHandler);
        var setMyCommandsResponse = bot.execute(new SetMyCommands(BojiaBotMyCommand.getBotCommands()));
        LOGGER.info(BojiaLogPrefixes.BOT_PREFIX + "set my commands response: {}", setMyCommandsResponse);
        return bot;
    }

    @Bean
    public BojiaBotUpdatesListener updatesListener() {
        return new BojiaBotUpdatesListener();
    }

    @Bean
    public BojiaBotExceptionHandler exceptionHandler() {
        return new BojiaBotExceptionHandler();
    }

}
