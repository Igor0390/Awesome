/*
package com.awesome.park.service.telegrammbot;

import com.awesome.park.util.BotState;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
@RequiredArgsConstructor
public class BotUpdateHandler {

    private final BotServiceHandler botServiceHandler;
    private final UserBotDataStorage userBotDataStorage;

    public void handleUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String chatId = message.getChatId().toString();
            long userId = message.getFrom().getId();
            String phoneNumber = userBotDataStorage.getUsersPhoneNumbers().getOrDefault(userId, "");
            String name = "Telegram: " + message.getFrom().getFirstName() + " " + message.getFrom().getLastName();

            BotState currentState = userBotDataStorage.getUsersBotStates().getOrDefault(userId, BotState.START);

            if (currentState == BotState.WAIT_FOR_BOOKING_TIME && isTimeInput(message.getText())) {
                handleBookingTimeInput(chatId, phoneNumber, name, message, userId);
            } else {
                handleOtherCommandsAndPhoneInput(chatId, phoneNumber, name, message, userId, currentState);
            }
        }
    }

    private void handleBookingTimeInput(String chatId, String phoneNumber, String name, Message message, long userId) {
        if (userBotDataStorage.getIsBookingTimeSelected().getOrDefault(userId, false)) {
            // Пользователь уже выбрал время, запрашиваем подтверждение
            executeSafely(botServiceHandler.sendConfirmationRequest(chatId));
        } else {
            // Обрабатываем ввод времени начала слота
            userBotDataStorage.getIsBookingTimeSelected().put(userId, true);
            executeSafely(botServiceHandler.handleBookingTimeInput(message, chatId, phoneNumber, name));
        }
    }

    private void handleOtherCommandsAndPhoneInput(String chatId, String phoneNumber, String name, Message message, long userId, BotState currentState) {
        switch (currentState) {
            case START -> {
                if (message.getText().equalsIgnoreCase("/booking")) {
                    executeSafely(botServiceHandler.handleStartCommand(chatId));
                    userBotDataStorage.getUsersBotStates().put(userId, BotState.WAIT_FOR_PHONE);
                }
            }
            case WAIT_FOR_PHONE ->
                    executeSafely(botServiceHandler.handlePhoneNumberInput(message, chatId, phoneNumber, name));
        }
    }

    private boolean isTimeInput(String input) {
        try {
            LocalTime.parse(input);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private void executeSafely(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}

*/
