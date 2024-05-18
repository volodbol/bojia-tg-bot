package com.volod.bojia.tg.service.bot;

import com.volod.bojia.tg.domain.vacancy.VacancyProvider;
import com.volod.bojia.tg.entity.BojiaBotUserSearch;
import com.volod.bojia.tg.entity.BojiaBotUserSearches;

public interface BojiaBotUserSearchService {
    BojiaBotUserSearches getByUserId(Long userId);

    BojiaBotUserSearch save(BojiaBotUserSearch search);

    boolean exists(Long userId, VacancyProvider provider, String keywords);

    void delete(Long userId, Long id);
}
