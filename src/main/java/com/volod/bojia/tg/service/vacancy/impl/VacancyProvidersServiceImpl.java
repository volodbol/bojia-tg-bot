package com.volod.bojia.tg.service.vacancy.impl;

import com.volod.bojia.tg.domain.vacancy.Vacancies;
import com.volod.bojia.tg.domain.vacancy.VacancyProvider;
import com.volod.bojia.tg.service.exception.BojiaExceptionHandlerService;
import com.volod.bojia.tg.service.vacancy.VacancyProviderService;
import com.volod.bojia.tg.service.vacancy.VacancyProvidersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.EnumMap;
import java.util.List;

@Service
public class VacancyProvidersServiceImpl implements VacancyProvidersService {

    private final EnumMap<VacancyProvider, VacancyProviderService> vacancyServices;

    @Autowired
    public VacancyProvidersServiceImpl(
            List<VacancyProviderService> vacancyProviderServices
    ) {
        this.vacancyServices = new EnumMap<>(VacancyProvider.class);
        vacancyProviderServices.forEach(service -> this.vacancyServices.put(service.getProvider(), service));
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
