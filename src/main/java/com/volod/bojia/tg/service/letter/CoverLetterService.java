package com.volod.bojia.tg.service.letter;

import com.volod.bojia.tg.domain.vacancy.Vacancy;
import com.volod.bojia.tg.entity.BojiaBotUser;

public interface CoverLetterService {
    String generateCoverLetter(BojiaBotUser user, Vacancy vacancy);
}
