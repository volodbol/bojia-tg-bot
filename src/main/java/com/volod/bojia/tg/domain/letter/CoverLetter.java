package com.volod.bojia.tg.domain.letter;

import com.volod.bojia.tg.domain.vacancy.Vacancy;

public record CoverLetter(
        Vacancy vacancy,
        String value
) {
}
