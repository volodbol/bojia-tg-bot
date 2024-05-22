package com.volod.bojia.tg.service.bot.impl;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.ReplyParameters;
import com.volod.bojia.tg.domain.bot.MessageMarkdownV2;
import com.volod.bojia.tg.domain.letter.VacancyCoverLetter;
import com.volod.bojia.tg.service.bot.BojiaBotService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BojiaBotServiceImpl implements BojiaBotService {

    private final TelegramBot bot;

    @Override
    public void sendVacancyCoverLetter(VacancyCoverLetter vacancyCoverLetter) {
        var user = vacancyCoverLetter.user();
        var sendResponse = this.bot.execute(
                MessageMarkdownV2.builder()
                        .chatId(user.getChatId())
                        .text(vacancyCoverLetter.vacancy().toString())
                        .build()
                        .toSendMessage()
        );
        this.bot.execute(
                MessageMarkdownV2.builder()
                        .chatId(user.getChatId())
                        .text(vacancyCoverLetter.value())
                        .build()
                        .toSendMessage()
                        .replyParameters(
                                new ReplyParameters(sendResponse.message().messageId(), user.getChatId())
                        )
        );
    }
}
