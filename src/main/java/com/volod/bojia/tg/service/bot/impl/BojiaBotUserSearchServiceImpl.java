package com.volod.bojia.tg.service.bot.impl;

import com.volod.bojia.tg.entity.BojiaBotUserSearch;
import com.volod.bojia.tg.entity.BojiaBotUserSearches;
import com.volod.bojia.tg.repository.BojiaBotUserSearchRepository;
import com.volod.bojia.tg.service.bot.BojiaBotUserSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BojiaBotUserSearchServiceImpl implements BojiaBotUserSearchService {

    private final BojiaBotUserSearchRepository userSearchRepository;

    @Override
    public BojiaBotUserSearches getByUserId(Long userId) {
        return new BojiaBotUserSearches(this.userSearchRepository.findAllByUserId(userId));
    }

    @Override
    public BojiaBotUserSearch save(BojiaBotUserSearch search) {
        return this.userSearchRepository.findByUserIdAndProviderAndKeywords(search)
                .orElseGet(() -> this.userSearchRepository.save(search));
    }

    @Override
    public void delete(Long userId, Long id) {
        this.userSearchRepository.deleteByUserIdAndId(userId, id);
    }
}
