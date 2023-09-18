package com.awesome.park.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.Instant;

@Data
public class BaseDto {
    @Schema(description = "уникальный идентификатор")
    private String uuid;

    @Schema(description = "телефон пользователя")
    private String phone;

    @Schema(description = "имя пользователя")
    private String name;

    @Schema(description = "время записи")

    private Instant time;
}
