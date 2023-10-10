package com.awesome.park.repository;

import com.awesome.park.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    Activity findByName(String activityName);
}
