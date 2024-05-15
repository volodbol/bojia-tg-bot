package com.volod.bojia.tg.service.vacancy;

import com.volod.bojia.tg.domain.vacancy.Vacancies;
import com.volod.bojia.tg.domain.vacancy.VacancyProvider;

import java.time.Instant;
import java.util.List;

public interface VacancyProviderService {
    Vacancies getLastVacancies(List<String> searchKeywords, Instant from);
    int getNumberOfVacancies(List<String> searchKeywords);
    VacancyProvider getProvider();
    String getUrl(List<String> searchKeywords);
    String getUrl();
}
