package com.awesome.park.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO для информации о Telegram")
public class TelegramInfoDto {

    @Schema(description = "Никнейм Пользователя Телеграм")
    private String username;

}

