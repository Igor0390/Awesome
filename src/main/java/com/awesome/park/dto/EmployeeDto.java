package com.awesome.park.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO для сотрудника")
public class EmployeeDto {

    @Schema(description = "Идентификатор сотрудника")
    private Long id;

    @Schema(description = "Имя сотрудника")
    private String firstName;

    @Schema(description = "Фамилия сотрудника")
    private String lastName;

    @Schema(description = "Роль сотрудника")
    private String role;

    @Schema(description = "Информация о Telegram сотрудника")
    private TelegramInfoDto telegramInfo;
}

