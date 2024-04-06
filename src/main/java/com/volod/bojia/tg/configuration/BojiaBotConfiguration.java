package com.volod.bojia.tg.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.GetMe;
import com.volod.bojia.tg.bot.handler.BojiaBotExceptionHandler;
import com.volod.bojia.tg.bot.listener.BojiaBotUpdatesListener;
import com.volod.bojia.tg.constant.BojiaLogConstants;
import com.volod.bojia.tg.property.BojiaApplicationProperties;
import com.volod.bojia.tg.service.bot.BojiaBotUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Slf4j
@Profile("dev")
@Configuration
@RequiredArgsConstructor
public class BojiaBotConfiguration {

    private final BojiaApplicationProperties applicationProperties;

    @Bean
    public TelegramBot bojiaBot() {
        var bot = new TelegramBot(this.applicationProperties.getBotToken());
        var getMeResponse = bot.execute(new GetMe());
        if (!getMeResponse.isOk()) {
            var ex = new IllegalArgumentException("Token is invalid. GetMe response - `%s`".formatted(getMeResponse));
            LOGGER.error(BojiaLogConstants.BOT_ERROR, ex);
            throw ex;
        }
        return bot;
    }

    @Bean
    public BojiaBotUpdatesListener updatesListener(
            BojiaBotUserService botUserService
    ) {
        return new BojiaBotUpdatesListener(
                botUserService
        );
    }

    @Bean
    public BojiaBotExceptionHandler exceptionHandler() {
        return new BojiaBotExceptionHandler();
    }

}
