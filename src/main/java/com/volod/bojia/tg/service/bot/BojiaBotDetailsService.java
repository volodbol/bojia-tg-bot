package com.volod.bojia.tg.service.bot;

import com.volod.bojia.tg.entity.BojiaBotDetails;

public interface BojiaBotDetailsService {
    BojiaBotDetails findOneOrCreate();
    void save(BojiaBotDetails botDetails);
}
