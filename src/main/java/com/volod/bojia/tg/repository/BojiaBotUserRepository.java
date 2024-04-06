package com.volod.bojia.tg.repository;

import com.volod.bojia.tg.entity.BojiaBotUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BojiaBotUserRepository extends JpaRepository<BojiaBotUser, Long> {

}