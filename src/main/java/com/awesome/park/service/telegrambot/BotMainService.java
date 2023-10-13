package com.awesome.park.service.telegrambot;


import com.awesome.park.config.botconfig.BotConfig;
import com.awesome.park.service.telegrambot.handlers.BaseBookingHandler;
import com.awesome.park.service.telegrambot.handlers.CallbackQueryHandler;
import com.awesome.park.service.telegrambot.handlers.SupBoardHandler;
import com.awesome.park.service.telegrambot.handlers.WakeBoardHandler;
import com.awesome.park.util.BotState;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;


@Component
@RequiredArgsConstructor
public class BotMainService extends TelegramLongPollingBot {

    private final BotConfig botConfig;
    private final UserBotDataStorage userBotDataStorage;
    private final InlineButtonKeyboard keyboard;
    private final CallbackQueryHandler callbackQueryHandler;
    private final WakeBoardHandler wakeBoardHandler;
    private final SupBoardHandler supBoardHandler;
    private final BaseBookingHandler baseBookingHandler;
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
            execute(callbackQueryHandler.handleCallbackQuery(update, chatId));
        }
    }

    @SneakyThrows
    private void stateBookingProcessing(Update update, Long chatId, String message, BotState currentState) {
        switch (currentState) {
            case START -> {
                if (message.equals("/start")) {
                    execute(keyboard.buildInlineButtonMenu(chatId));
                }
            }
            case WAKE_WAIT_FOR_NAME_AND_SURNAME -> execute(baseBookingHandler.checkNameAndSurname(update, BotState.WAKE_WAIT_FOR_PHONE));
            case WAKE_WAIT_FOR_PHONE -> execute(baseBookingHandler.checkPhone(update, chatId, BotState.WAKE_WAIT_FOR_BOOKING_TIME));
            case WAKE_WAIT_FOR_BOOKING_TIME -> execute(wakeBoardHandler.buildBookingTimeButtonMenu(chatId, " ", BotState.WAKE_WAIT_FOR_CONFIRMATION));
            case WAKE_WAIT_FOR_CONFIRMATION -> execute(wakeBoardHandler.checkConfirmation(update));

            case SUP_BOARD_WAIT_FOR_NAME_AND_SURNAME -> execute(baseBookingHandler.checkNameAndSurname(update, BotState.SUP_BOARD_WAIT_FOR_PHONE));
            case SUP_BOARD_WAIT_FOR_PHONE -> execute(baseBookingHandler.checkPhone(update, chatId, BotState.SUP_BOARD_WAIT_FOR_SUP_BOOKING_TIME));
            case SUP_BOARD_WAIT_FOR_SUP_BOOKING_TIME -> execute(supBoardHandler.buildSupTimeButtonMenu(chatId," " , BotState.SUP_BOARD_WAIT_FOR_CONFIRMATION));
            case SUP_BOARD_WAIT_FOR_CONFIRMATION -> execute(supBoardHandler.checkConfirmation(update));

            case STOP_BOT -> execute(returnToInitialState(chatId));
            // Другие состояния
        }
    }
    public SendMessage returnToInitialState(Long chatId) {
        // Очистить состояние пользователя в хранилище
        userBotDataStorage.getUsersBotStates().remove(chatId);
        String response = "Я завершил свою работу и вернулся в начальное состояние!";
        return new SendMessage(chatId.toString(), response);
    }


}




