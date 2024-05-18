package com.volod.bojia.tg.domain.bot;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BojiaBotCallback {
    SEARCH_SAVED("ss"),
    SEARCH_REMOVED("sr");

    private final String value;

    public static String data(BojiaBotCallback callback, Long data) {
        return data(callback, data.toString());
    }

    public static String data(BojiaBotCallback callback, String data) {
        return "%s_%s".formatted(callback.value, data);
    }

    public int length() {
        return this.value.length() + 1;
    }
}
