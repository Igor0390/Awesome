package com.awesome.park.service.telegrammbot;


import com.awesome.park.config.botconfig.BotConfig;
import com.awesome.park.service.telegrammbot.handlers.CallbackQueryHandler;
import com.awesome.park.service.telegrammbot.handlers.EventsHandler;
import com.awesome.park.service.telegrammbot.handlers.WakeBoardBookingHandler;
import com.awesome.park.util.BotState;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
@RequiredArgsConstructor
public class BotMainService extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final UserBotDataStorage userBotDataStorage;
    private final InlineButtonKeyboard keyboard;
    private final CallbackQueryHandler callbackQueryHandler;
    private final WakeBoardBookingHandler bookingHandler;
    private final EventsHandler eventsHandler;
    private Long chatId;

    @Override
    public String getBotUsername() {
        return botConfig.name();
    }

    @Override
    public String getBotToken() {
        return botConfig.token();
    }

    @Override
    @SneakyThrows
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            chatId = update.getMessage().getChatId();
            String message = update.getMessage().getText();
            // Получение текущего состояния пользователя из хранилища
            BotState currentState = userBotDataStorage.getUsersBotStates().getOrDefault(chatId, BotState.START);
            // Обработка состояний
            stateBookingProcessing(update, chatId, message, currentState);
        } else if (update.hasCallbackQuery()) {
            execute(callbackQueryHandler.handleCallbackQuery(update,chatId));
        }
    }

    @SneakyThrows
    private void stateBookingProcessing(Update update, Long chatId , String message, BotState currentState) {
        switch (currentState) {
            case START -> {
                if (message.equals("/start")) {
                    execute(keyboard.buildInlineButtonMenu(chatId));
                }
            }
            case WAIT_FOR_NAME_AND_SURNAME -> {
                execute(bookingHandler.checkNameAndSurname(update));
            }
            case WAIT_FOR_PHONE -> {
                execute(bookingHandler.checkPhone(update,chatId));
            }
            case WAIT_FOR_BOOKING_TIME -> {
                execute(bookingHandler.buildBookingTimeButtonMenu(chatId));
            }
            case WAIT_FOR_CONFIRMATION -> {
                execute(bookingHandler.checkConfirmation(update));
            }

            // Другие состояния
        }
    }
}




