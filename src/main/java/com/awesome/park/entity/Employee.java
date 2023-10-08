package com.awesome.park.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "employees")
@Data
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Имя сотрудника не может быть пустым")
    @Size(min = 2, message = "Имя сотрудника должно содержать минимум 2 символа")
    private String firstName;

    @NotBlank(message = "Фамилия сотрудника не может быть пустой")
    @Size(min = 2, message = "Фамилия сотрудника должна содержать минимум 2 символа")
    private String lastName;

    @NotBlank(message = "Роль сотрудника не может быть пустой")
    private String role;

    @OneToOne
    @JoinColumn(name = "telegram_info_id")
    private TelegramInfo telegramInfo;
}
