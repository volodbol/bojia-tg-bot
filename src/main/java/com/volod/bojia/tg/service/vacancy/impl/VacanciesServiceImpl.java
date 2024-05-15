package com.volod.bojia.tg.service.vacancy.impl;

import com.volod.bojia.tg.domain.vacancy.Vacancies;
import com.volod.bojia.tg.domain.vacancy.VacancyProvider;
import com.volod.bojia.tg.service.exception.BojiaExceptionHandlerService;
import com.volod.bojia.tg.service.vacancy.VacanciesService;
import com.volod.bojia.tg.service.vacancy.VacancyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class VacanciesServiceImpl implements VacanciesService {

    private final ConcurrentMap<VacancyProvider, VacancyService> vacancyServices;

    @Autowired
    public VacanciesServiceImpl(
            BojiaExceptionHandlerService exceptionHandlerService
    ) {
        this.vacancyServices = new ConcurrentHashMap<>();
        this.vacancyServices.put(
                VacancyProvider.DJINNI,
                new DjinniVacancyService(
                        exceptionHandlerService
                )
        );
    }

    @Override
    public Vacancies getLastVacancies(VacancyProvider provider, List<String> searchKeywords, Instant from) {
        return this.vacancyServices.get(provider).getLastVacancies(searchKeywords, from);
    }

    @Override
    public int getNumberOfVacancies(VacancyProvider provider, List<String> searchKeywords) {
        return this.vacancyServices.get(provider).getNumberOfVacancies(searchKeywords);
    }

}
