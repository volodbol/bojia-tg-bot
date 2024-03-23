package com.volod.bojia.tg.service.bot.impl;

import com.volod.bojia.tg.entity.BojiaBotDetails;
import com.volod.bojia.tg.repository.BojiaBotDetailsRepository;
import com.volod.bojia.tg.service.bot.BojiaBotDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class BojiaBotDetailsServiceImpl implements BojiaBotDetailsService {

    private final BojiaBotDetailsRepository bojiaBotDetailsRepository;

    @Override
    public BojiaBotDetails findOneOrCreate() {
        var details = this.bojiaBotDetailsRepository.findAll();
        return isEmpty(details) ? new BojiaBotDetails() : details.get(0);
    }

    @Override
    public void save(BojiaBotDetails botDetails) {
        this.bojiaBotDetailsRepository.save(botDetails);
    }

}
