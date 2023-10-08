package com.awesome.park.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;


@Entity
@Table(name = "customers")
@Data
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Имя не может быть пустым")
    @Size(min = 2, message = "Имя должно содержать не менее {min} символов")
    @Pattern(regexp = "^(?!\\s*$)[\\p{L} .'-]+$", message = "Некорректный формат имени")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank(message = "Фамилия не может быть пустой")
    @Size(min = 2, message = "Фамилия должна содержать не менее {min} символов")
    @Pattern(regexp = "^(?!\\s*$)[\\p{L} .'-]+$", message = "Некорректный формат фамилии")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotBlank(message = "Номер телефона не может быть пустым")
    @Pattern(regexp = "^(\\+7|8)\\d{10}$", message = "Некорректный формат номера телефона")
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "telegram_info_id")
    private TelegramInfo telegramInfo;

    @OneToMany(mappedBy = "customer")
    private Set<Booking> bookings;
}
