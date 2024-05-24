package com.volod.bojia.tg.domain.letter;

import com.volod.bojia.tg.entity.BojiaBotUserSearch;

import java.time.Instant;
import java.util.List;

public record VacanciesCoverLetters(
        BojiaBotUserSearch botUserSearch,
        List<VacancyCoverLetter> values,
        Instant lastPublishedVacancy
) {

    public static VacanciesCoverLetters of(
            BojiaBotUserSearch botUserSearch,
            List<VacancyCoverLetter> vacancyCoverLetters
    ) {
        return new VacanciesCoverLetters(
                botUserSearch,
                vacancyCoverLetters,
                vacancyCoverLetters.stream()
                        .map(letter -> letter.vacancy().published())
                        .max(Instant::compareTo)
                        .orElseGet(Instant::now)
        );
    }

}
