package com.awesome.park.service.telegrambot;

import com.awesome.park.util.BotState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InlineButtonKeyboard {


    private final UserBotDataStorage userBotDataStorage;

    public InlineKeyboardMarkup createInlineKeyboardMarkup() {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        List<InlineKeyboardButton> row2 = new ArrayList<>();
        List<InlineKeyboardButton> row3 = new ArrayList<>();

        // Добавляем новые кнопки
        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Записаться на каталку");
        button1.setCallbackData("wake_boarding_booking");

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("Арендовать сап-борд");
        button2.setCallbackData("rent_sup_board");

        InlineKeyboardButton button3 = new InlineKeyboardButton();
        button3.setText("Посмотреть ближайшие события");
        button3.setCallbackData("events");

        row1.add(button1);
        row2.add(button2);
        row3.add(button3);

        keyboardRows.add(row1);
        keyboardRows.add(row2);
        keyboardRows.add(row3);
        keyboard.setKeyboard(keyboardRows);

        return keyboard;
    }

    public SendMessage buildInlineButtonMenu(long chatId) {
        InlineKeyboardMarkup keyboard = createInlineKeyboardMarkup();
        return SendMessage.builder()
                .chatId(chatId)
                .text("Привет, я помогу тебе сделать следующие вещи:")
                .parseMode("Markdown")
                .replyMarkup(keyboard)
                .build();
    }

    public InlineKeyboardMarkup createSupBoardAmountKeyboard(Long chatId) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();

        // Создаем первую строку для цифр 1 и 2
        List<InlineKeyboardButton> firstRow = new ArrayList<>();
        firstRow.add(createSupBoardAmountButton("1", "1"));
        firstRow.add(createSupBoardAmountButton("2", "2"));
        keyboardRows.add(firstRow);

        // Создаем вторую строку для цифр 3, 4 и 5
        List<InlineKeyboardButton> secondRow = new ArrayList<>();
        secondRow.add(createSupBoardAmountButton("3", "3"));
        secondRow.add(createSupBoardAmountButton("4", "4"));
        secondRow.add(createSupBoardAmountButton("5", "5"));
        keyboardRows.add(secondRow);

        // Создаем третью строку для цифр 6, 7 и 8
        List<InlineKeyboardButton> thirdRow = new ArrayList<>();
        thirdRow.add(createSupBoardAmountButton("6", "6"));
        thirdRow.add(createSupBoardAmountButton("7", "7"));
        thirdRow.add(createSupBoardAmountButton("8", "8"));
        keyboardRows.add(thirdRow);

        keyboard.setKeyboard(keyboardRows);
        userBotDataStorage.getUsersBotStates().put(chatId, BotState.SUP_BOARD_WAIT_FOR_CONFIRMATION);

        return keyboard;
    }

    private InlineKeyboardButton createSupBoardAmountButton(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData("BOARD_COUNT:" + callbackData);
        return button;
    }


}

