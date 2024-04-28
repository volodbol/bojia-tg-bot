package com.volod.bojia.tg.domain.exception;

import com.pengrad.telegrambot.model.Update;

public class BojiaBotUpdateIllegal extends Exception {

    public BojiaBotUpdateIllegal(Update update) {
        super("Illegal update: " + update);
    }

}
