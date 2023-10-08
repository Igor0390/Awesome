package com.awesome.park.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "DTO для активности")
public class ActivityDto {

    @Schema(description = "Идентификатор активности")
    private Long id;

    @Schema(description = "Наименование активности")
    private String name;

    @Schema(description = "Стоимость активности (в рублях)")
    private double price;
}

