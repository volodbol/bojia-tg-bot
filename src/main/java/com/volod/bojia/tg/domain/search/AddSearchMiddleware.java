package com.volod.bojia.tg.domain.search;

import com.pengrad.telegrambot.model.Update;
import com.volod.bojia.tg.domain.vacancy.VacancyProvider;

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

    public abstract boolean check(Update update, VacancyProvider provider);

    protected boolean checkNext(Update update, VacancyProvider provider) {
        if (isNull(this.next)) {
            return true;
        }
        return this.next.check(update, provider);
    }
}
