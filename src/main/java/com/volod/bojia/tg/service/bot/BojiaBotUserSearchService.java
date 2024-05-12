package com.volod.bojia.tg.service.bot;

import com.volod.bojia.tg.entity.BojiaBotUserSearches;

public interface BojiaBotUserSearchService {
    BojiaBotUserSearches getByUserId(Long userId);
    void delete(Long userId, Long id);
}
