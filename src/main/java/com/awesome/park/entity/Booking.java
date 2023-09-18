package com.awesome.park.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.Instant;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Booking {
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String phone;
    private String name;
    private Instant time;
}
