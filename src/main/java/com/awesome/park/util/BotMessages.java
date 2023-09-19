package com.awesome.park.util;

public record BotMessages(String enterPhoneMessage, String invalidPhoneMessage, String selectTimeMessage) {
    public static final BotMessages MESSAGES = new BotMessages(
            "Введите ваш номер телефона:",
           "Ввели некоректные данные! Попробуйте еще раз! " + "введите номер телефона в формате  +7********** или 8**********",
            "Выберите время начала вашей каталки:");
}

