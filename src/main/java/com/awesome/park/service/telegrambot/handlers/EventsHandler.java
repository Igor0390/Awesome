package com.awesome.park.service.telegrambot.handlers;

import com.awesome.park.service.telegrambot.UserBotDataStorage;
import com.awesome.park.util.BotState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class EventsHandler {

    private final UserBotDataStorage userBotDataStorage;

    public SendMessage handleEvents(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        String chatId = callbackQuery.getMessage().getChatId().toString();
        String response = "Пока тут ничего нет. Но здесь будут ближайшие события и мероприятия которые будут проходить в парке.";
        userBotDataStorage.getUsersBotStates().put(callbackQuery.getMessage().getChatId(), BotState.STOP_BOT);

        return new SendMessage(chatId, response);
    }
}
