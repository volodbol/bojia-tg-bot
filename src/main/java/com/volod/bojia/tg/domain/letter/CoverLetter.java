package com.volod.bojia.tg.domain.letter;

import com.volod.bojia.tg.domain.vacancy.Vacancy;
import com.volod.bojia.tg.entity.BojiaBotUser;

public record CoverLetter(
        BojiaBotUser user,
        Vacancy vacancy,
        String value
) {
}
