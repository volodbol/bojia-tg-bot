package com.volod.bojia.tg.service.bot.impl;

import com.volod.bojia.tg.entity.BojiaBotUser;
import com.volod.bojia.tg.repository.BojiaBotUserSearchRepository;
import com.volod.bojia.tg.service.bot.BojiaBotUserSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BojiaBotUserSearchServiceImpl implements BojiaBotUserSearchService {

    private final BojiaBotUserSearchRepository userSearchRepository;

    @Override
    public void delete(BojiaBotUser user, Long id) {
        this.userSearchRepository.deleteByUserAndId(user, id);
    }
}
