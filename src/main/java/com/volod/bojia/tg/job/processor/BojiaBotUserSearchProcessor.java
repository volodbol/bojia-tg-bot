package com.volod.bojia.tg.job.processor;

import com.google.common.util.concurrent.Futures;
import com.volod.bojia.tg.domain.letter.VacanciesCoverLetters;
import com.volod.bojia.tg.domain.letter.VacancyCoverLetter;
import com.volod.bojia.tg.domain.vacancy.Vacancy;
import com.volod.bojia.tg.entity.BojiaBotUserSearch;
import com.volod.bojia.tg.service.letter.CoverLetterService;
import com.volod.bojia.tg.service.vacancy.VacancyProvidersService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;

import java.util.concurrent.ExecutorService;
import java.util.function.Function;

@RequiredArgsConstructor
public class BojiaBotUserSearchProcessor implements ItemProcessor<BojiaBotUserSearch, VacanciesCoverLetters> {

    private final VacancyProvidersService vacancyProvidersService;
    private final CoverLetterService coverLetterService;
    private final ExecutorService executor;

    @Override
    public VacanciesCoverLetters process(BojiaBotUserSearch search) {
        var user = search.getUser();
        var vacancies = this.vacancyProvidersService.getLastVacancies(
                search.getProvider(),
                search.getKeywordsSplit(),
                search.getLastPublished()
        );
        Function<Vacancy, VacancyCoverLetter> coverLetterFnc
                = vacancy -> this.coverLetterService.generateVacancyCoverLetter(user, vacancy);
        return VacanciesCoverLetters.of(
                search,
                vacancies.values().stream()
                        .map(vacancy -> this.executor.submit(() -> coverLetterFnc.apply(vacancy)))
                        .map(Futures::getUnchecked)
                        .toList()
        );
    }

}
