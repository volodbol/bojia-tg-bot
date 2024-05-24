package com.volod.bojia.tg.service.search.impl;

import com.volod.bojia.tg.domain.vacancy.VacancyProvider;
import com.volod.bojia.tg.entity.BojiaBotUserSearch;
import com.volod.bojia.tg.domain.search.BojiaBotUserSearches;
import com.volod.bojia.tg.repository.BojiaBotUserSearchRepository;
import com.volod.bojia.tg.service.search.BojiaBotUserSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BojiaBotUserSearchServiceImpl implements BojiaBotUserSearchService {

    private final BojiaBotUserSearchRepository userSearchRepository;

    @Override
    public BojiaBotUserSearches getByUserId(Long userId) {
        return new BojiaBotUserSearches(this.userSearchRepository.findAllByUserId(userId));
    }

    @Override
    public List<BojiaBotUserSearch> saveAll(List<BojiaBotUserSearch> searches) {
        return this.userSearchRepository.saveAll(searches);
    }

    @Override
    public BojiaBotUserSearch save(BojiaBotUserSearch search) {
        return this.userSearchRepository.save(search);
    }

    @Override
    public boolean exists(Long userId, VacancyProvider provider, String keywords) {
        return this.userSearchRepository.existsByUserIdAndProviderAndKeywords(userId, provider, keywords);
    }

    @Override
    public void delete(Long userId, Long id) {
        this.userSearchRepository.deleteByUserIdAndId(userId, id);
    }
}
