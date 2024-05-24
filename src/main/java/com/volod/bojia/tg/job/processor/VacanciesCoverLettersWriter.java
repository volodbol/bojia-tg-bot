package com.volod.bojia.tg.job.processor;

import com.volod.bojia.tg.domain.letter.VacanciesCoverLetters;
import com.volod.bojia.tg.service.bot.BojiaBotService;
import com.volod.bojia.tg.service.search.BojiaBotUserSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

@RequiredArgsConstructor
public class VacanciesCoverLettersWriter implements ItemWriter<VacanciesCoverLetters> {

    private final BojiaBotUserSearchService botUserSearchService;
    private final BojiaBotService botService;

    @Override
    public void write(Chunk<? extends VacanciesCoverLetters> chunk) {
        var items = chunk.getItems();
        var searches = items.stream()
                .map(letters -> {
                    var search = letters.botUserSearch();
                    search.setLastPublished(letters.lastPublishedVacancy());
                    return search;
                })
                .toList();
        this.botUserSearchService.saveAll(searches);
        items.stream()
                .flatMap(letters -> letters.values().stream())
                .forEach(this.botService::sendVacancyCoverLetter);
    }

}
