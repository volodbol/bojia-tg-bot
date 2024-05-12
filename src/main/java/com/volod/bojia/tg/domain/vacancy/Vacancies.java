package com.volod.bojia.tg.domain.vacancy;

import java.time.Instant;
import java.util.Set;

public record Vacancies(
        Set<Vacancy> values,
        Instant lastPublished
) {

    public static Vacancies empty() {
        return new Vacancies(Set.of(), Instant.now());
    }

    public static Vacancies of(Set<Vacancy> vacancies) {
        return new Vacancies(
                vacancies,
                vacancies.stream().map(Vacancy::published).max(Instant::compareTo).orElseGet(Instant::now)
        );
    }
}
