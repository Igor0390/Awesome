package com.awesome.park.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Schema(description = "DTO для бронирования")
public class BookingDto {

    @Schema(description = "Идентификатор бронирования")
    private Long id;

    @Schema(description = "Идентификатор активности, на которую произведено бронирование")
    private Long activityId;


    @Schema(description = "Время бронирования")
    private LocalDateTime bookingTime;
}

