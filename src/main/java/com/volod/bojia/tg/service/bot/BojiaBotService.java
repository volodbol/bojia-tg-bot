package com.volod.bojia.tg.service.bot;

import com.volod.bojia.tg.domain.letter.CoverLetter;

public interface BojiaBotService {
    void sendCoverLetter(CoverLetter coverLetter);
}
