package com.volod.bojia.tg.domain.vacancy;

import com.volod.bojia.tg.domain.bot.BojiaBotCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum VacancyProvider {
    DJINNI("Djinni", BojiaBotCommand.DJINNI);

    private final String readableName;
    private final BojiaBotCommand botCommand;

}
