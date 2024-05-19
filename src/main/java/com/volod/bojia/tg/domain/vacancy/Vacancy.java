package com.volod.bojia.tg.domain.vacancy;

import java.time.Instant;
import java.util.Comparator;
import java.util.LinkedList;

public record Vacancy(
        String company,
        String title,
        LinkedList<String> shortDetails,
        String description,
        String url,
        Instant published
) {
    public static final Comparator<Vacancy> NEWEST = Comparator.comparing(Vacancy::published).reversed();
}
