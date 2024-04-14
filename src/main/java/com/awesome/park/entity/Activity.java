package com.awesome.park.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
//entity
@Entity
@Data
@Table(name = "activities")
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Наименование активности не может быть пустым")
    @Size(min = 2, message = "Наименование активности должно содержать минимум 2 символа")
    private String name;

    @NotNull(message = "Стоимость активности не может быть пустой")
    @DecimalMin(value = "100.00", message = "Стоимость активности должна быть не меньше 100р")
    private BigDecimal price;

}

