package com.volod.bojia.tg.service.letter;

import com.volod.bojia.tg.domain.letter.CoverLetter;
import com.volod.bojia.tg.domain.vacancy.Vacancy;
import com.volod.bojia.tg.entity.BojiaBotUser;

public interface CoverLetterService {
    CoverLetter generateCoverLetter(BojiaBotUser user, Vacancy vacancy);
}
