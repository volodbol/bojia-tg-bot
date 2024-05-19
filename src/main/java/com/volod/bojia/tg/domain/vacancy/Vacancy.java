package com.volod.bojia.tg.domain.vacancy;

import java.time.Instant;
import java.util.Comparator;

public record Vacancy(
        String company,
        String title,
        String shortDetails,
        String description,
        String url,
        Instant published
) {
    public static final Comparator<Vacancy> NEWEST = Comparator.comparing(Vacancy::published).reversed();
}
