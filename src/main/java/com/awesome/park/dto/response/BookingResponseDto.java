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

    @Schema(description = "время записи")

    private Instant time;
}
