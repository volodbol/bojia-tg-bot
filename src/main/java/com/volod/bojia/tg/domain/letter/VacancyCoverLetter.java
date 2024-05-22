package com.volod.bojia.tg.domain.letter;

import com.volod.bojia.tg.domain.vacancy.Vacancy;
import com.volod.bojia.tg.entity.BojiaBotUser;

public record VacancyCoverLetter(
        BojiaBotUser user,
        Vacancy vacancy,
        String value
) {
}
