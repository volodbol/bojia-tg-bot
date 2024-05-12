package com.volod.bojia.tg.domain.exception;

import com.volod.bojia.tg.domain.vacancy.VacancyProvider;
import org.jsoup.nodes.Element;

public class BojiaVacancyParseException extends Exception {

    public BojiaVacancyParseException(String message, VacancyProvider provider, Throwable cause) {
        super(
                "%s. Provider: %s".formatted(message, provider),
                cause
        );
    }

    public static BojiaVacancyParseException getVacancies(VacancyProvider provider, Throwable cause) {
        return new BojiaVacancyParseException("Can't get last vacancies", provider, cause);
    }

    public static BojiaVacancyParseException getNumberOfVacancies(VacancyProvider provider, Throwable cause) {
        return new BojiaVacancyParseException("Can't get number of vacancies", provider, cause);
    }

    public static BojiaVacancyParseException parseError(Element element, VacancyProvider provider, Throwable cause) {
        return new BojiaVacancyParseException("Can't parse vacancy. %s".formatted(element), provider, cause);
    }

}
