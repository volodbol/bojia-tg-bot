package com.volod.bojia.tg.domain.vacancy;

import java.time.Instant;
import java.util.Set;

public record Vacancies(
        Set<Vacancy> values,
        Instant lastPublished
) {
}
