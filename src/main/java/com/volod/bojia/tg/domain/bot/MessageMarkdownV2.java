package com.volod.bojia.tg.domain.bot;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.regex.Pattern;

import static java.util.Objects.nonNull;

@Getter
@EqualsAndHashCode
@ToString
public class MessageMarkdownV2 {
    private final Object chatId;
    private final String value;

    private MessageMarkdownV2(Object chatId, String value) {
        this.chatId = chatId;
        this.value = value;
    }

    public static MessageMarkdownV2Builder builder() {
        return new MessageMarkdownV2Builder();
    }

    public SendMessage toSendMessage() {
        return new SendMessage(this.chatId, this.value).parseMode(ParseMode.MarkdownV2);
    }

    public static class MessageMarkdownV2Builder {
        private static final Pattern ESCAPE_PATTERN = Pattern.compile("([_*\\[\\]()~`>#+\\-=|{}.!])");

        private Object chatId;
        private final StringBuilder builder;

        private MessageMarkdownV2Builder() {
            this.builder = new StringBuilder();
        }

        private String escape(String value) {
            return ESCAPE_PATTERN.matcher(value).replaceAll("\\\\$1");
        }

        public MessageMarkdownV2Builder chatId(Object chatId) {
            this.chatId = chatId;
            return this;
        }

        public MessageMarkdownV2Builder chatId(Update update) {
            if (nonNull(update.message())) {
                this.chatId = update.message().chat().id();
            } else if (nonNull(update.callbackQuery())) {
                this.chatId = update.callbackQuery().chatInstance();
            } else {
                throw new IllegalArgumentException("ChatId is unavailable");
            }
            return this;
        }

        public MessageMarkdownV2Builder text(String value) {
            this.builder.append(this.escape(value));
            return this;
        }

        public MessageMarkdownV2Builder inlineCode(String value) {
            this.builder.append("`%s`".formatted(this.escape(value)));
            return this;
        }

        public MessageMarkdownV2Builder bold(String value) {
            this.builder.append("*%s*".formatted(this.escape(value)));
            return this;
        }

        public MessageMarkdownV2 build() {
            if (nonNull(this.chatId)) {
                var message = this.builder.toString();
                return new MessageMarkdownV2(this.chatId, message);
            } else {
                throw new IllegalArgumentException("ChatId is null");
            }
        }

    }
}
