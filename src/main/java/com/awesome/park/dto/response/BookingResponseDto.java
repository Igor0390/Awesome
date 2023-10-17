package com.awesome.park.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Schema(description = "ResponseDto пользователя")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseDto {
    @Schema(description = "уникальный идентификатор")
    private String id;

    @Schema(description = "телефон пользователя")
    private String phone;

    @Schema(description = "имя пользователя")
    private String name;

    @Schema(description = "фамилия пользователя")
    private String surname;

    @Schema(description = "Только Калининградское время записи")
    private Instant time;

    @Schema(description = "Тип услуги")
    private Long activityId;

    @Schema(description = "Количество забронированных услуг")
    private Integer activityCount;
}
