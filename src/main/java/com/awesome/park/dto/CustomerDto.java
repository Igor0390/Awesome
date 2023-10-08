package com.awesome.park.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO для клиента")
public class CustomerDto {

    @Schema(description = "Идентификатор клиента")
    private Long id;

    @Schema(description = "Имя клиента")
    private String firstName;

    @Schema(description = "Фамилия клиента")
    private String lastName;

    @Schema(description = "Номер телефона клиента")
    private String phoneNumber;

    @Schema(description = "Информация о Telegram клиента")
    private TelegramInfoDto telegramInfo;
}
