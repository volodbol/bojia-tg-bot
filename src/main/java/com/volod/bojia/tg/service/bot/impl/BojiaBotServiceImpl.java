package com.volod.bojia.tg.service.bot.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ReplyParameters;
import com.volod.bojia.tg.domain.bot.MessageMarkdownV2;
import com.volod.bojia.tg.domain.letter.VacanciesCoverLetters;
import com.volod.bojia.tg.domain.letter.VacancyCoverLetter;
import com.volod.bojia.tg.entity.BojiaBotUserSearch;
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
    public void sendVacanciesCoverLetters(VacanciesCoverLetters vacanciesCoverLetters) {
        var search = vacanciesCoverLetters.botUserSearch();
        vacanciesCoverLetters.values().forEach(letter -> this.sendVacancyCoverLetter(search, letter));
    }

    @Override
    public void sendVacancyCoverLetter(BojiaBotUserSearch search, VacancyCoverLetter letter) {
        try {
            var user = letter.user();
            var vacancy = letter.vacancy();
            var vacancyMessage = MessageMarkdownV2.builder()
                    .chatId(user.getChatId())
                    .bold(vacancy.title())
                    .text(" at %s".formatted(vacancy.company()))
                    .italic("%n%s".formatted(vacancy.shortDetails()))
                    .text("%n%s...".formatted(vacancy.getTrimmedDescription(255)))
                    .text("%n%s".formatted(vacancy.url()))
                    .italic("%nSubscription: ".formatted())
                    .inlineCode(search.getKeywords())
                    .italic(" (%s)".formatted(search.getProvider().getReadableName()))
                    .build()
                    .toSendMessage()
                    .disableNotification(true);
            var letterMessage = MessageMarkdownV2.builder()
                    .chatId(user.getChatId())
                    .text("Generated cover letter for this vacancy:")
                    .text("%n%n%s".formatted(letter.value()))
                    .build()
                    .toSendMessage()
                    .disableNotification(true);
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
