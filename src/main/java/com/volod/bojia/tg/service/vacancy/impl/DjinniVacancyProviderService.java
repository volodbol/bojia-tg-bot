package com.volod.bojia.tg.service.vacancy.impl;

import com.volod.bojia.tg.domain.exception.BojiaVacancyParseException;
import com.volod.bojia.tg.domain.vacancy.Vacancies;
import com.volod.bojia.tg.domain.vacancy.Vacancy;
import com.volod.bojia.tg.domain.vacancy.VacancyProvider;
import com.volod.bojia.tg.service.exception.BojiaExceptionHandlerService;
import com.volod.bojia.tg.service.vacancy.VacancyProviderService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.volod.bojia.tg.constant.JsoupConstants.*;

// TODO [VB] implement retry mechanism
@Slf4j
@Service
public class DjinniVacancyProviderService implements VacancyProviderService {

    private static final DateTimeFormatter DJINNI_DTF = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")
            .withZone(ZoneId.systemDefault());

    // Services
    private final BojiaExceptionHandlerService exceptionHandlerService;
    // Jsoup
    private final Connection connection;
    private final Document.OutputSettings outputSettings;

    @Autowired
    public DjinniVacancyProviderService(
            BojiaExceptionHandlerService exceptionHandlerService
    ) {
        this.exceptionHandlerService = exceptionHandlerService;
        this.connection = Jsoup.connect(this.getUrl());
        this.outputSettings = new Document.OutputSettings();
        this.outputSettings.prettyPrint(false);
    }

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
            var page = this.connection.newRequest(this.getUrl(searchKeywords)).get();
            page.outputSettings(this.outputSettings);
            page.select("br").before("\\n");
            page.select("p").before("\\n");
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
        var vacancies = new ArrayList<Vacancy>();
        for (var element : elements) {
            try {
                var counts = element.getElementsByAttributeValueStarting(CLASS, "job-list-item__counts");
                var published = counts.first().getElementsByAttribute(TITLE).attr(TITLE);
                var publishedTime = Instant.from(DJINNI_DTF.parse(published));
                if (publishedTime.isBefore(from)) {
                    continue;
                }
                var company = element.getElementsByAttributeValueStarting(CLASS, "job-list-item__pic");
                var companyText = company.parents().first().text();
                var title = element.getElementsByAttributeValueStarting(CLASS, "job-list-item__title");
                var titleText = title.text();
                var url = title.first().getElementsByAttribute(HREF).attr(HREF);
                var shortDetails = element.getElementsByAttributeValueStarting(CLASS, "job-list-item__job-info");
                var shortDetailsList = shortDetails.text();
                var description = element.getElementsByAttributeValueStarting(ID, "job-description");
                var descriptionText = description.text().replace("\\n", "\n");
                vacancies.add(
                        new Vacancy(
                                companyText,
                                titleText,
                                shortDetailsList,
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
