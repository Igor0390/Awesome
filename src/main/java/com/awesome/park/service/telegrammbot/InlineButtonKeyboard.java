package com.awesome.park.service.telegrammbot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InlineButtonKeyboard {


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
                .text("Привет, вы можете сделать следующие вещи:")
                .parseMode("Markdown")
                .replyMarkup(keyboard)
                .build();
    }


}

