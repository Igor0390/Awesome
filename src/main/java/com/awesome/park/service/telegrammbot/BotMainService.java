package com.awesome.park.service.telegrammbot;


import com.awesome.park.config.botconfig.BotConfig;
import com.awesome.park.util.BotState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Component
@RequiredArgsConstructor
public class BotMainService extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final UserBotDataStorage userBotDataStorage;
    private final BotServiceHandler botServiceHandler;

    @Override
    public String getBotUsername() {
        return botConfig.name();
    }

    @Override
    public String getBotToken() {
        return botConfig.token();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String chatId = message.getChatId().toString();
            String phoneNumber = userBotDataStorage.getUsersPhoneNumbers().getOrDefault(message.getFrom().getId(), "");
            String name = "Telegram: " + update.getMessage().getFrom().getFirstName() + " " + update.getMessage().getFrom().getLastName();

            BotState currentState = userBotDataStorage.getUsersBotStates().getOrDefault(message.getFrom().getId(), BotState.START);

            switch (currentState) {
                case START -> {
                    if (message.getText().equalsIgnoreCase("/booking")) {
                        executeSafely(botServiceHandler.handleStartCommand(chatId));
                        userBotDataStorage.getUsersBotStates().put(message.getFrom().getId(), BotState.WAIT_FOR_PHONE);
                    }
                }
                case WAIT_FOR_PHONE ->
                        executeSafely(botServiceHandler.handlePhoneNumberInput(message, chatId, phoneNumber, name));
                case WAIT_FOR_BOOKING_TIME ->
                        executeSafely(botServiceHandler.handleBookingTimeInput(message, chatId, phoneNumber, name));
            }
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


