package com.volod.bojia.tg.domain.vacancy;

import java.time.Instant;
import java.util.List;

public record Vacancies(
        List<Vacancy> values,
        Instant lastPublished
) {

    public static Vacancies empty() {
        return new Vacancies(List.of(), Instant.now());
    }

    public static Vacancies of(List<Vacancy> vacancies) {
        return new Vacancies(
                vacancies.stream()
                        .sorted(Vacancy.NEWEST)
                        .toList(),
                vacancies.stream()
                        .map(Vacancy::published)
                        .max(Instant::compareTo)
                        .orElseGet(Instant::now)
        );
    }
}
