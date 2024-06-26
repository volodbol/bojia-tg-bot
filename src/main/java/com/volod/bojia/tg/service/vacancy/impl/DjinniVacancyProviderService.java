package com.volod.bojia.tg.service.vacancy.impl;

import com.google.common.util.concurrent.Futures;
import com.volod.bojia.tg.domain.exception.BojiaVacancyParseException;
import com.volod.bojia.tg.domain.vacancy.Vacancies;
import com.volod.bojia.tg.domain.vacancy.Vacancy;
import com.volod.bojia.tg.domain.vacancy.VacancyProvider;
import com.volod.bojia.tg.service.exception.BojiaExceptionHandlerService;
import com.volod.bojia.tg.service.vacancy.VacancyProviderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.volod.bojia.tg.constant.JsoupConstants.*;

@Slf4j
@Service
public class DjinniVacancyProviderService implements VacancyProviderService {

    private static final DateTimeFormatter DJINNI_DTF = DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy")
            .withZone(ZoneId.systemDefault());

    // Retry
    private final RetryTemplate retryTemplate;
    // Services
    private final ExecutorService executor;
    private final BojiaExceptionHandlerService exceptionHandlerService;
    // Jsoup
    private final Connection connection;
    private final Document.OutputSettings outputSettings;

    @Autowired
    public DjinniVacancyProviderService(
            RetryTemplate retryTemplate,
            BojiaExceptionHandlerService exceptionHandlerService
    ) {
        this.retryTemplate = retryTemplate;
        // WARNING: 15 vacancies per page, with check rate 30 minutes, usually no more than 5 vacancies expected
        this.executor = Executors.newFixedThreadPool(
                5,
                new BasicThreadFactory.Builder()
                        .namingPattern("djinniVacancyProviderService-%d")
                        .build()
        );
        this.exceptionHandlerService = exceptionHandlerService;
        this.connection = Jsoup.connect(this.getUrl());
        this.outputSettings = new Document.OutputSettings();
        this.outputSettings.prettyPrint(false);
    }

    /**
     * Returns last vacancies, usually available from the first page.
     *
     * @param searchKeywords keywords to search from job
     * @param from           time to return last vacancies from
     * @return last vacancies with time > from
     */
    @Override
    public Vacancies getLastVacancies(List<String> searchKeywords, Instant from) {
        try {
             var page = this.retryTemplate.execute(context -> {
                var document = this.connection.newRequest(this.getUrl(searchKeywords)).get();
                document.outputSettings(this.outputSettings);
                document.select("br").before("\\n");
                document.select("p").before("\\n");
                return document;
            });
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
            var page = this.retryTemplate.execute(context -> {
                var response = Jsoup.connect(this.getUrl(searchKeywords)).execute();
                return response.parse();
            });
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
    private Vacancies getVacancies(Elements elements, Instant from) {
        return Vacancies.of(
                elements.stream()
                        .map(element -> this.executor.submit(() -> this.getVacancy(element, from)))
                        .map(Futures::getUnchecked)
                        .filter(Objects::nonNull)
                        .toList()
        );
    }

    @SuppressWarnings("DataFlowIssue")
    @Nullable
    private Vacancy getVacancy(Element element, Instant from) {
        try {
            var counts = element.getElementsByAttributeValueStarting(CLASS, "job-list-item__counts");
            var published = counts.first().getElementsByAttribute(TITLE).attr(TITLE);
            var publishedTime = Instant.from(DJINNI_DTF.parse(published));
            if (publishedTime.isBefore(from) || publishedTime.equals(from)) {
                return null;
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
            return new Vacancy(
                    companyText,
                    titleText,
                    shortDetailsList,
                    descriptionText,
                    this.getUrl() + url.replace("/jobs/", ""),
                    publishedTime
            );
        } catch (RuntimeException ex) {
            this.exceptionHandlerService.publishException(
                    BojiaVacancyParseException.parseError(element, this.getProvider(), ex)
            );
            return null;
        }
    }

}
