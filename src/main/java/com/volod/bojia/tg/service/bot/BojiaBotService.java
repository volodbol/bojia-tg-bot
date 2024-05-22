package com.volod.bojia.tg.service.bot;

import com.volod.bojia.tg.domain.letter.VacancyCoverLetter;

public interface BojiaBotService {
    void sendVacancyCoverLetter(VacancyCoverLetter vacancyCoverLetter);
}
