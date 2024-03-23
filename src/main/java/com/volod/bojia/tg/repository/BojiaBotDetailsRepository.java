package com.volod.bojia.tg.repository;

import com.volod.bojia.tg.entity.BojiaBotDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BojiaBotDetailsRepository extends JpaRepository<BojiaBotDetails, Long> {

}
