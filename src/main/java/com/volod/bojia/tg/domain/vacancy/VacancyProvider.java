package com.volod.bojia.tg.domain.vacancy;

import com.volod.bojia.tg.domain.bot.BojiaBotMyCommand;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VacancyProvider {
    DJINNI(BojiaBotMyCommand.DJINNI);

    private final BojiaBotMyCommand botCommand;

}
