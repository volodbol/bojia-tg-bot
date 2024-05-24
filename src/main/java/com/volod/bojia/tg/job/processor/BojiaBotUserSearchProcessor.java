package com.volod.bojia.tg.job.processor;

import com.volod.bojia.tg.domain.letter.VacanciesCoverLetters;
import com.volod.bojia.tg.entity.BojiaBotUserSearch;
import com.volod.bojia.tg.service.letter.CoverLetterService;
import com.volod.bojia.tg.service.vacancy.VacancyProvidersService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;

@RequiredArgsConstructor
public class BojiaBotUserSearchProcessor implements ItemProcessor<BojiaBotUserSearch, VacanciesCoverLetters> {

    private final VacancyProvidersService vacancyProvidersService;
    private final CoverLetterService coverLetterService;

    @Override
    public VacanciesCoverLetters process(BojiaBotUserSearch search) {
        var user = search.getUser();
        var vacancies = this.vacancyProvidersService.getLastVacancies(
                search.getProvider(),
                search.getKeywordsSplit(),
                search.getLastPublished()
        );
        return VacanciesCoverLetters.of(
                search,
                vacancies.values().stream()
                        .map(vacancy -> this.coverLetterService.generateVacancyCoverLetter(user, vacancy))
                        .toList()
        );
    }

}
