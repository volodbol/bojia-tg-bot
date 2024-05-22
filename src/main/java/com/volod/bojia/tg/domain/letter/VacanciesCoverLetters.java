package com.volod.bojia.tg.domain.letter;

import com.volod.bojia.tg.entity.BojiaBotUserSearch;

import java.util.List;

public record VacanciesCoverLetters(
        BojiaBotUserSearch botUserSearch,
        List<VacancyCoverLetter> values
) {
}
