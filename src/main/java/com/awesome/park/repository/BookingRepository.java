package com.awesome.park.repository;


import com.awesome.park.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Booking findByCustomerId(Long customerId);

    List<Booking> findByActivityId(Long activityId);

    Booking findByCustomerIdAndActivityId(Long customerId,Long activityId);
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.activityId = :activityId AND b.bookingTime = :bookingTime")
    int countBookedSupBoardsAtTime(Long activityId, LocalDateTime bookingTime);

    @Query("SELECT b.bookingTime FROM Booking b WHERE b.activityId = :activityId GROUP BY b.bookingTime HAVING COUNT(b) < :maxCapacity")
    List<LocalDateTime> getBookedTimeSlots(Long activityId, Long maxCapacity);
}
