package com.volod.bojia.tg.service.bot;

import com.volod.bojia.tg.domain.letter.VacanciesCoverLetters;
import com.volod.bojia.tg.domain.letter.VacancyCoverLetter;
import com.volod.bojia.tg.entity.BojiaBotUserSearch;

public interface BojiaBotService {
    void sendVacanciesCoverLetters(VacanciesCoverLetters vacanciesCoverLetters);
    void sendVacancyCoverLetter(BojiaBotUserSearch search, VacancyCoverLetter letter);
}
