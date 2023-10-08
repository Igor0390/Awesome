package com.awesome.park.repository;

import com.awesome.park.entity.TelegramInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TelegramInfoRepository extends JpaRepository<TelegramInfo, Long> {
}
