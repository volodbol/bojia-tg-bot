package com.volod.bojia.tg.domain.search;

import com.volod.bojia.tg.entity.BojiaBotUserSearch;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.CollectionUtils.isEmpty;

public record BojiaBotUserSearches(List<BojiaBotUserSearch> values) {

    public String getKeywordsOrDefault() {
        if (isEmpty(this.values)) {
            return "You have no searches";
        } else {
            return this.values.stream()
                    .map(search -> "%s - %s".formatted(search.getId(), search.getKeywords()))
                    .collect(Collectors.joining(",\n"));
        }
    }

}
