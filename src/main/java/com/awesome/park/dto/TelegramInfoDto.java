package com.awesome.park.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO для информации о Telegram")
public class TelegramInfoDto {

    @Schema(description = "Идентификатор чата Телеграм")
    private Long chatId;

    @Schema(description = "Никнейм Пользователя Телеграм")
    private String username;

    @Schema(description = "Тип сущности (клиент или сотрудник)")
    private String entityType;

    @Schema(description = "Идентификатор сущности, с которой связана информация о Телеграме")
    private Long entityId;
}

