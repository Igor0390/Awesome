package com.awesome.park.service.telegrambot.handlers;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class CallbackQueryHandler {

    private final WakeBoardHandler bookingHandler;
    private final SupBoardHandler supBoardHandler;
    private final EventsHandler eventsHandler;

    @SneakyThrows
    public SendMessage handleCallbackQuery(Update update, Long chatId) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        String callbackData = callbackQuery.getData();
        if (callbackData.startsWith("TIME_SLOT:")) {
            return bookingHandler.handleTimeSlotCallback(chatId, callbackData);
        } else if( callbackData.startsWith("SUP_BOARD_TIME_SLOT:")){
            return supBoardHandler.createSupBoardKeyboard(chatId, callbackData);
        }else if( callbackData.startsWith("BOARD_COUNT:")){
            return supBoardHandler.checkConfirmation(update);
        }
        switch (callbackData) {
            case "wake_boarding_booking" -> {
                // Вызываем обработчик для записи на катание на вейк-борде
                return bookingHandler.handleWakeBoardBooking(update,chatId);
            }
            case "rent_sup_board" -> {
                // Вызываем обработчик для аренды сап-бордов
                return supBoardHandler.handleSupBoardBooking(update, chatId);
            }
            case "events" -> {
                // Вызываем обработчик для отображения ближайших событий
                return eventsHandler.handleEvents(update);
            }
            default -> {
                // Обработка других callback-запросов, на будущее
            }
        }
        return new SendMessage(chatId.toString(), "что-то пошло не так");
    }
}

