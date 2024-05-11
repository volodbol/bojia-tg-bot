package com.volod.bojia.tg.service.vacancy.impl;

import com.volod.bojia.tg.domain.vacancy.Vacancies;
import com.volod.bojia.tg.domain.vacancy.VacancyProvider;
import com.volod.bojia.tg.service.vacancy.VacancyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DjinniVacancyService implements VacancyService {

    private final Clock clock;

    @Override
    public Vacancies getLastPublishedVacancies(List<String> searchKeywords, Instant lastFetch) {
        return new Vacancies(new HashSet<>(), Instant.now(this.clock));
    }

    @Override
    public int getNumberOfVacancies(List<String> searchKeywords) {
        try {
            var page = Jsoup.connect(this.getUrl(searchKeywords)).execute().parse();
            var pageHeader = page.body().getElementsByClass("page-header");
            var splitHeader = pageHeader.eachText().get(0).split(" ");
            return Integer.parseInt(splitHeader[splitHeader.length - 1]);
        } catch (IOException | RuntimeException ex) {
            LOGGER.debug("Can't get number of vacancies", ex);
            return 0;
        }
    }

    @Override
    public VacancyProvider getProvider() {
        return VacancyProvider.DJINNI;
    }

    @Override
    public String getUrl(List<String> searchKeywords) {
        var keywords = String.join("+", searchKeywords);
        return "%s?all-keywords=%s&keywords=%s&title_only=on".formatted(
                this.getUrl(),
                keywords,
                keywords
        );
    }

    @Override
    public String getUrl() {
        return "https://djinni.co/jobs/";
    }

}
