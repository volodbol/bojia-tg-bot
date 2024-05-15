package com.volod.bojia.tg.domain.search;

import com.pengrad.telegrambot.model.Update;
import com.volod.bojia.tg.domain.bot.BojiaBotCommand;

import java.util.List;

import static java.util.Objects.isNull;

public abstract class AddSearchMiddleware {
    private AddSearchMiddleware next;

    public static AddSearchMiddleware link(AddSearchMiddleware first, List<AddSearchMiddleware> chain) {
        var head = first;
        for (var nextInChain : chain) {
            head.next = nextInChain;
            head = nextInChain;
        }
        return first;
    }

    public abstract boolean check(Update update, BojiaBotCommand command);

    protected boolean checkNext(Update update, BojiaBotCommand command) {
        if (isNull(this.next)) {
            return true;
        }
        return this.next.check(update, command);
    }
}
