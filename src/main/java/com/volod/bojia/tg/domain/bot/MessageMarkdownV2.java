package com.volod.bojia.tg.domain.bot;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.*;

import java.util.regex.Pattern;

import static java.util.Objects.nonNull;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
@ToString
public class MessageMarkdownV2 {
    private final Object chatId;
    private final Integer messageId;
    private final String value;

    public static MessageMarkdownV2Builder builder() {
        return new MessageMarkdownV2Builder();
    }

    public SendMessage toSendMessage() {
        return new SendMessage(this.chatId, this.value).parseMode(ParseMode.MarkdownV2);
    }

    public EditMessageText toEditMessageText() {
        return new EditMessageText(this.chatId, this.messageId, this.value).parseMode(ParseMode.MarkdownV2);
    }

    public static class MessageMarkdownV2Builder {
        private static final Pattern ESCAPE_PATTERN = Pattern.compile("([_*\\[\\]()~`>#+\\-=|{}.!])");

        private Object chatId;
        private Integer messageId;
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
                return this.chatId(update.message().chat().id());
            } else if (nonNull(update.callbackQuery())) {
                return this.chatId(update.callbackQuery().maybeInaccessibleMessage().chat().id());
            } else {
                throw new IllegalArgumentException("ChatId is unavailable");
            }
        }

        public MessageMarkdownV2Builder messageId(Integer messageId) {
            this.messageId = messageId;
            return this;
        }

        public MessageMarkdownV2Builder messageId(Update update) {
            if (nonNull(update.message())) {
                return this.messageId(update.message().messageId());
            } else if (nonNull(update.callbackQuery())) {
                return this.messageId(update.callbackQuery().maybeInaccessibleMessage().messageId());
            } else {
                throw new IllegalArgumentException("MessageId is unavailable");
            }
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
                return new MessageMarkdownV2(
                        this.chatId,
                        this.messageId,
                        message
                );
            } else {
                throw new IllegalArgumentException("ChatId is null");
            }
        }

    }
}
