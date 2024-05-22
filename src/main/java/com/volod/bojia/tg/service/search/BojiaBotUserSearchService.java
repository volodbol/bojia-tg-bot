package com.volod.bojia.tg.service.search;

import com.volod.bojia.tg.domain.vacancy.VacancyProvider;
import com.volod.bojia.tg.entity.BojiaBotUserSearch;
import com.volod.bojia.tg.entity.BojiaBotUserSearches;

import java.util.List;

public interface BojiaBotUserSearchService {
    BojiaBotUserSearches getByUserId(Long userId);

    List<BojiaBotUserSearch> saveAll(List<BojiaBotUserSearch> searches);
    BojiaBotUserSearch save(BojiaBotUserSearch search);

    boolean exists(Long userId, VacancyProvider provider, String keywords);

    void delete(Long userId, Long id);
}
