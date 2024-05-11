package com.volod.bojia.tg.service.vacancy;

import com.volod.bojia.tg.domain.vacancy.Vacancies;
import com.volod.bojia.tg.domain.vacancy.VacancyProvider;

import java.time.Instant;
import java.util.List;

public interface VacancyService {
    Vacancies getLastPublishedVacancies(List<String> searchKeywords, Instant lastFetch);
    VacancyProvider getProvider();
    String getUrl();
}
