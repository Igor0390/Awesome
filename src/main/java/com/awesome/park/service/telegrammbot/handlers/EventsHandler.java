package com.awesome.park.service.telegrammbot.handlers;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class EventsHandler {

    public SendMessage handleEvents(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        // Здесь вы можете реализовать логику для отображения ближайших событий или мероприятий
        // Получите информацию о событиях из базы данных и подготовьте сообщение для пользователя
        String chatId = callbackQuery.getMessage().getChatId().toString();
        String response = "Здесь будут ближайшие события и мероприятия.";

        return new SendMessage(chatId, response);
    }
}
