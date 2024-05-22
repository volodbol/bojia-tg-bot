package com.volod.bojia.tg.domain.letter;

import com.volod.bojia.tg.entity.BojiaBotUserSearch;

import java.util.List;

public record CoverLetters(
        BojiaBotUserSearch botUserSearch,
        List<CoverLetter> coverLetters
) {
}
