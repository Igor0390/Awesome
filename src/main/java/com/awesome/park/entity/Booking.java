package com.awesome.park.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Data
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @JoinColumn(name = "activity_id", nullable = false)
    private Long activityId;


    @JoinColumn(name = "customer_id", nullable = false)
    private Long customerId;


    @JoinColumn(name = "employee_id")
    private Long employeeId;

    @NotNull(message = "Время бронирования не может быть пустым")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "booking_time", nullable = false)
    private LocalDateTime bookingTime;

    @Column(name = "activity_count")
    private Integer activityCount;
}

