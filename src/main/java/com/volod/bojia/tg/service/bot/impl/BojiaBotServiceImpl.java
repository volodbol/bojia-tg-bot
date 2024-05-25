package com.volod.bojia.tg.service.bot.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ReplyParameters;
import com.volod.bojia.tg.domain.bot.MessageMarkdownV2;
import com.volod.bojia.tg.domain.letter.VacancyCoverLetter;
import com.volod.bojia.tg.service.bot.BojiaBotService;
import com.volod.bojia.tg.service.exception.BojiaExceptionHandlerService;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BojiaBotServiceImpl implements BojiaBotService {

    private final RetryTemplate retryTemplate;
    private final BojiaExceptionHandlerService exceptionHandlerService;
    private final TelegramBot bot;

    @Override
    public void sendVacancyCoverLetter(VacancyCoverLetter vacancyCoverLetter) {
        try {
            var user = vacancyCoverLetter.user();
            var vacancyMessage = MessageMarkdownV2.builder()
                    .chatId(user.getChatId())
                    .text(vacancyCoverLetter.vacancy().toString())
                    .build()
                    .toSendMessage();
            var letterMessage = MessageMarkdownV2.builder()
                    .chatId(user.getChatId())
                    .text(vacancyCoverLetter.value())
                    .build()
                    .toSendMessage();
            this.retryTemplate.execute(context -> {
                var vacancyResponse = this.bot.execute(vacancyMessage);
                if (!vacancyResponse.isOk()) {
                    throw new IllegalArgumentException("Message with vacancy wasn't sent");
                }
                var letterResponse = this.bot.execute(
                        letterMessage.replyParameters(
                                new ReplyParameters(vacancyResponse.message().messageId(), user.getChatId())
                        )
                );
                if (!letterResponse.isOk()) {
                    throw new IllegalArgumentException("Message with vacancy cover letter wasn't sent");
                }
                return true;
            });
        } catch (RuntimeException ex) {
            this.exceptionHandlerService.publishException(ex);
        }
    }
}
