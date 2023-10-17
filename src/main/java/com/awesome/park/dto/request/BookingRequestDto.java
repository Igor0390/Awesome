package com.awesome.park.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "RequestDto пользователя")

@Data
public class BookingRequestDto {
    private String id;

    @Schema(description = "телефон пользователя")
    private String phone;

    @Schema(description = "имя пользователя")
    private String name;

    @Schema(description = "фамилия пользователя")
    private String surname;

    @Schema(description = "время записи")
    private LocalDateTime time;

    @Schema(description = "тип услуги")
    private Long activityId;

    @Schema(description = "количество бронирований")
    private Integer activityCount;
}
