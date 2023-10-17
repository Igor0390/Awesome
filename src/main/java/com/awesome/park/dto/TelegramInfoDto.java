package com.awesome.park.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO для информации о Telegram")
public class TelegramInfoDto {
    @Schema(description = "ид пользователя")
    private Long id;
    @Schema(description = "Чат ид пользователя")
    private Long chatId;
    @Schema(description = "Никнейм Пользователя Телеграм")
    private String username;
}

