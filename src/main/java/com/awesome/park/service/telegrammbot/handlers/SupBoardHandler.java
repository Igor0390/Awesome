package com.awesome.park.service.telegrammbot.handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class SupBoardHandler {

    public SendMessage handleSupBoardBooking(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();

        // Здесь вы можете реализовать логику обработки аренды сап-бордов
        // Получите информацию о пользователе из update и выполните необходимые действия
        // Например, запросите у пользователя количество сап-бордов и дату аренды и сохраните в базу данных
        String chatId = callbackQuery.getMessage().getChatId().toString();
        String response = "Вы выбрали аренду сап-бордов. Пожалуйста, укажите количество сап-бордов и дату аренды.";

        return new SendMessage(chatId, response);
    }
}

