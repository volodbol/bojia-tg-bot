package com.volod.bojia.tg.repository;

import com.volod.bojia.tg.entity.BojiaBotUser;
import com.volod.bojia.tg.entity.BojiaBotUserSearch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BojiaBotUserSearchRepository extends JpaRepository<BojiaBotUserSearch, Long> {
    void deleteByUserAndId(BojiaBotUser user, Long id);
}