package com.awesome.park.service.telegrambot;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

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

    public InlineKeyboardMarkup createEmployeeRoleKeyboard(){
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();
        List<InlineKeyboardButton> row1 = new ArrayList<>();

        InlineKeyboardButton button1 = new InlineKeyboardButton();
        button1.setText("Администратор");
        button1.setCallbackData("role_administrator");

        InlineKeyboardButton button2 = new InlineKeyboardButton();
        button2.setText("Оператор лебедки");
        button2.setCallbackData("role_operator");

        row1.add(button1);
        row1.add(button2);
        keyboardRows.add(row1);
        keyboard.setKeyboard(keyboardRows);
        return keyboard;
    }

    public SendMessage buildUserInlineButtonMenu(long chatId) {
        InlineKeyboardMarkup keyboard = createInlineKeyboardMarkup();
        return SendMessage.builder()
                .chatId(chatId)
                .text("Привет, я помогу тебе сделать следующие вещи:")
                .parseMode("Markdown")
                .replyMarkup(keyboard)
                .build();
    }

    public SendMessage buildEmployeeInlineButtonMenu(long chatId) {
        InlineKeyboardMarkup keyboard = createEmployeeRoleKeyboard();
        return SendMessage.builder()
                .chatId(chatId)
                .text("Привет, коллега! Выбери свою роль: ")
                .parseMode("Markdown")
                .replyMarkup(keyboard)
                .build();
    }

    public ReplyKeyboardMarkup getReplyKeyboardMarkup(String firstButtonText, String secondButtonText) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();

        KeyboardButton yesButton = new KeyboardButton(firstButtonText);
        KeyboardButton noButton = new KeyboardButton(secondButtonText);

        row.add(yesButton);
        row.add(noButton);

        keyboard.add(row);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

}

