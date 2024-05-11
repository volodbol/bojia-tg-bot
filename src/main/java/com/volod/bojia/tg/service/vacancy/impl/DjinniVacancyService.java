package com.volod.bojia.tg.service.vacancy.impl;

import com.volod.bojia.tg.domain.vacancy.Vacancies;
import com.volod.bojia.tg.domain.vacancy.VacancyProvider;
import com.volod.bojia.tg.service.vacancy.VacancyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DjinniVacancyService implements VacancyService {

    private final Clock clock;

    @Override
    public Vacancies getLastPublishedVacancies(List<String> searchKeywords, Instant lastFetch) {
        return new Vacancies(new HashSet<>(), Instant.now(this.clock));
    }

    @Override
    public VacancyProvider getProvider() {
        return VacancyProvider.DJINNI;
    }

    @Override
    public String getUrl() {
        return "https://djinni.co/jobs/";
    }

}
