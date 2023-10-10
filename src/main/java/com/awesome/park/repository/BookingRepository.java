package com.awesome.park.repository;


import com.awesome.park.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

/*    List<Booking> findByPhoneAndName(String phone, String name);

    @Query("SELECT b FROM Booking b WHERE b.time >= :startTime AND b.time < :endTime")
    List<Booking> findByTimeBetween(@Param("startTime") Instant startTime, @Param("endTime") Instant endTime);*/
}
