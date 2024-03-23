package com.volod.bojia.tg.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.volod.bojia.tg.bot.BojiaBotExceptionHandler;
import com.volod.bojia.tg.bot.BojiaBotUpdatesListener;
import com.volod.bojia.tg.property.BojiaApplicationProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
@RequiredArgsConstructor
public class BojiaTelegramBotConfiguration {

    private final BojiaApplicationProperties applicationProperties;

    @Bean
    TelegramBot telegramBot(
            BojiaBotUpdatesListener updatesListener,
            BojiaBotExceptionHandler exceptionHandler
    ) {
        var telegramBot = new TelegramBot(this.applicationProperties.getBotToken());
        telegramBot.setUpdatesListener(updatesListener, exceptionHandler);
        return telegramBot;
    }

    @Bean
    BojiaBotUpdatesListener updatesListener() {
        return new BojiaBotUpdatesListener();
    }

    @Bean
    BojiaBotExceptionHandler exceptionHandler() {
        return new BojiaBotExceptionHandler();
    }

}
