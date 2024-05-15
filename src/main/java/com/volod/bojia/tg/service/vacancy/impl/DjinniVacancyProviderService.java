package com.volod.bojia.tg.service.vacancy.impl;

import com.volod.bojia.tg.domain.exception.BojiaVacancyParseException;
import com.volod.bojia.tg.domain.vacancy.Vacancies;
import com.volod.bojia.tg.domain.vacancy.Vacancy;
import com.volod.bojia.tg.domain.vacancy.VacancyProvider;
import com.volod.bojia.tg.service.exception.BojiaExceptionHandlerService;
import com.volod.bojia.tg.service.vacancy.VacancyProviderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import static com.volod.bojia.tg.constant.JsoupConstants.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DjinniVacancyProviderService implements VacancyProviderService {

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")
            .withZone(ZoneId.systemDefault());

    private final BojiaExceptionHandlerService exceptionHandlerService;

    /**
     * Returns last vacancies, usually available from the first page.
     *
     * @param searchKeywords keywords to search from job
     * @param from time to return last vacancies from
     * @return last vacancies with time > from
     */
    @Override
    public Vacancies getLastVacancies(List<String> searchKeywords, Instant from) {
        try {
            var page = Jsoup.connect(this.getUrl(searchKeywords)).execute().parse();
            var jobs = page.getElementsByAttributeValueStarting("id", "job-item-");
            return this.getVacancies(jobs, from);
        } catch (IOException | RuntimeException ex) {
            this.exceptionHandlerService.publishException(
                    BojiaVacancyParseException.getVacancies(this.getProvider(), ex)
            );
            return Vacancies.empty();
        }
    }

    @Override
    public int getNumberOfVacancies(List<String> searchKeywords) {
        try {
            var page = Jsoup.connect(this.getUrl(searchKeywords)).execute().parse();
            var pageHeader = page.body().getElementsByClass("page-header");
            var splitHeader = pageHeader.eachText().get(0).split(" ");
            return Integer.parseInt(splitHeader[splitHeader.length - 1]);
        } catch (IOException | RuntimeException ex) {
            this.exceptionHandlerService.publishException(
                    BojiaVacancyParseException.getNumberOfVacancies(this.getProvider(), ex)
            );
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

    // -----------------------------------------------------------------------------------------------------------------
    // PRIVATE METHODS
    // -----------------------------------------------------------------------------------------------------------------
    @SuppressWarnings("DataFlowIssue")
    private Vacancies getVacancies(Elements elements, Instant from) {
        var vacancies = new HashSet<Vacancy>();
        for (var element : elements) {
            try {
                var counts = element.getElementsByAttributeValueStarting(CLASS, "job-list-item__counts");
                var published = counts.first().getElementsByAttribute(TITLE).attr(TITLE);
                var publishedTime = Instant.from(DTF.parse(published));
                if (publishedTime.isBefore(from)) {
                    continue;
                }
                var company = element.getElementsByAttributeValueStarting(CLASS, "job-list-item__pic");
                var companyText = company.parents().first().text();
                var title = element.getElementsByAttributeValueStarting(CLASS, "job-list-item__title");
                var titleText = title.text();
                var url = title.first().getElementsByAttribute(HREF).attr(HREF);
                var description = element.getElementsByAttributeValueStarting(ID, "job-description");
                var descriptionText = description.attr("data-original-text");
                var shortDetails = element.getElementsByAttributeValueStarting(CLASS, "job-list-item__job-info");
                var shortDetailsList = shortDetails.eachText();
                vacancies.add(
                        new Vacancy(
                                companyText,
                                titleText,
                                new LinkedList<>(shortDetailsList),
                                descriptionText,
                                this.getUrl() + url.replace("/jobs/", ""),
                                publishedTime
                        )
                );
            } catch (RuntimeException ex) {
                this.exceptionHandlerService.publishException(
                        BojiaVacancyParseException.parseError(element, this.getProvider(), ex)
                );
            }
        }
        return Vacancies.of(vacancies);
    }

}
