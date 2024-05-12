package com.volod.bojia.tg.repository;

import com.volod.bojia.tg.entity.BojiaBotUserSearch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BojiaBotUserSearchRepository extends JpaRepository<BojiaBotUserSearch, Long> {
    List<BojiaBotUserSearch> findAllByUserId(Long userId);
    void deleteByUserIdAndId(Long userId, Long id);
}