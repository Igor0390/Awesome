package com.awesome.park.service.telegrambot.handlers;

import com.awesome.park.entity.TelegramInfo;
import com.awesome.park.service.CustomerService;
import com.awesome.park.service.TelegramInfoService;
import com.awesome.park.service.telegrambot.UserBotDataStorage;
import com.awesome.park.util.BotState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.awesome.park.service.telegrambot.handlers.BaseBookingHandler.findedCustomer;
import static com.awesome.park.service.telegrambot.handlers.BaseBookingHandler.telegramUserName;


@Component
@RequiredArgsConstructor

public class SupBoardHandler {
    private final TelegramInfoService telegramInfoService;
    private final CustomerService customerService;
    private final UserBotDataStorage userBotDataStorage;

    public SendMessage handleSupBoardBooking(Update update, Long chatId) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        //получим никнейм юзера из телеги
        telegramUserName = callbackQuery.getFrom().getUserName();
        // Попробуем найти пользователя по userName (нику в телеге)
        TelegramInfo user = telegramInfoService.findByUsername(telegramUserName);
        String response;
        if (user != null) {
            // Пользователь найден, предложим выбрать дату и время бронирования
            findedCustomer = customerService.getCustomerByTelegramInfoId(user.getId());
            return buildSupTimeButtonMenu(chatId, " дружище я тебя помню ! ", BotState.SUP_BOARD_WAIT_FOR_CONFIRMATION);
        } else {
            // Пользователь не найден, создаем нового
            response = "Прежде чем записаться и катать давай познакомимся! Введи пожалуйста свое Имя и Фамилию";
            userBotDataStorage.getUsersBotStates().put(callbackQuery.getMessage().getChatId(), BotState.SUP_BOARD_WAIT_FOR_NAME_AND_SURNAME);

        }
        return new SendMessage(chatId.toString(), response);
    }


    public SendMessage buildSupTimeButtonMenu(Long chatId, String textMessage, BotState botState) {
        return new SendMessage(chatId.toString(), "создаем кнопки для сапов" + textMessage);
    }

    public SendMessage checkConfirmation(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        return new SendMessage(chatId, "подтвердили сапы");

    }
}

