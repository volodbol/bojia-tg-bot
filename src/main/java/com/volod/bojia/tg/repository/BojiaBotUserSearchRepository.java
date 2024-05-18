package com.volod.bojia.tg.repository;

import com.volod.bojia.tg.domain.vacancy.VacancyProvider;
import com.volod.bojia.tg.entity.BojiaBotUserSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BojiaBotUserSearchRepository extends JpaRepository<BojiaBotUserSearch, Long> {
    List<BojiaBotUserSearch> findAllByUserId(Long userId);
    boolean existsByUserIdAndProviderAndKeywords(Long userId, VacancyProvider provider, String keywords);
    @Transactional
    void deleteByUserIdAndId(Long userId, Long id);
}