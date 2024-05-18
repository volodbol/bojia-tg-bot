package com.volod.bojia.tg.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.GetMe;
import com.volod.bojia.tg.bot.handler.BojiaBotExceptionHandler;
import com.volod.bojia.tg.bot.listener.BojiaBotUpdatesListener;
import com.volod.bojia.tg.constant.BojiaLogConstants;
import com.volod.bojia.tg.domain.exception.BojiaBotInitializationException;
import com.volod.bojia.tg.property.BojiaApplicationProperties;
import com.volod.bojia.tg.service.bot.BojiaBotUpdateService;
import com.volod.bojia.tg.service.exception.BojiaExceptionHandlerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.List;

@Slf4j
@Profile("dev")
@Configuration
@RequiredArgsConstructor
public class BojiaBotConfiguration {

    private final BojiaApplicationProperties applicationProperties;

    @Bean
    public TelegramBot bojiaBot() throws BojiaBotInitializationException {
        var bot = new TelegramBot(this.applicationProperties.getBotToken());
        try {
            var getMeResponse = bot.execute(new GetMe());
            LOGGER.trace(BojiaLogConstants.BOT_PREFIX + "getMeResponse: {}", getMeResponse);
            if (!getMeResponse.isOk()) {
                throw new IllegalArgumentException("Token is invalid. GetMe response - [%s]".formatted(getMeResponse));
            }
        } catch (RuntimeException ex) {
            throw new BojiaBotInitializationException("Can't initialize bojia bot", ex);
        }
        return bot;
    }

    @Bean
    public BojiaBotUpdatesListener updatesListener(
            List<BojiaBotUpdateService> updateServices,
            BojiaExceptionHandlerService exceptionHandlerService
    ) {
        return new BojiaBotUpdatesListener(
                updateServices,
                exceptionHandlerService
        );
    }

    @Bean
    public BojiaBotExceptionHandler exceptionHandler(
            BojiaExceptionHandlerService bojiaExceptionHandlerService
    ) {
        return new BojiaBotExceptionHandler(
                bojiaExceptionHandlerService
        );
    }

}
