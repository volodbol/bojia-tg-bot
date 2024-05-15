package com.volod.bojia.tg.domain.vacancy;

import com.volod.bojia.tg.domain.bot.BojiaBotCommand;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VacancyProvider {
    DJINNI(BojiaBotCommand.DJINNI);

    private final BojiaBotCommand botCommand;

}
