package com.volod.bojia.tg.service.vacancy;

import com.volod.bojia.tg.domain.vacancy.Vacancies;
import com.volod.bojia.tg.domain.vacancy.VacancyProvider;

import java.time.Instant;
import java.util.List;

public interface VacancyProvidersService {
    Vacancies getLastVacancies(VacancyProvider provider, List<String> searchKeywords, Instant from);
    int getNumberOfVacancies(VacancyProvider provider, List<String> searchKeywords);
}
