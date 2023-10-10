/*
package com.awesome.park.service.telegrammbot;

import com.awesome.park.service.TelegramInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.generics.TelegramBot;

@Service
@RequiredArgsConstructor
public class TelegramBotService {
    private final TelegramBot telegramBot;
    private final TelegramInfoService telegramUserService;

    // ...

    public void handleUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            long chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();

            // ...

            if (currentState == BotState.WAIT_FOR_PHONE_NUMBER) {
                // Обработка номера телефона
                if (isValidPhoneNumber(messageText)) {
                    // Сохраняем номер телефона в базу данных
                    telegramUserService.saveUserPhoneNumber(chatId, messageText);

                    // Отправляем сообщение с предложением выбрать активность
                    telegramBot.sendMessage(new SendMessage(chatId, "Спасибо! Теперь выберите активность:"));

                    // Переводим пользователя в состояние выбора активности
                    telegramUserService.setUserState(chatId, BotState.WAIT_FOR_ACTIVITY_CHOICE);
                } else {
                    telegramBot.sendMessage(new SendMessage(chatId, "Пожалуйста, введите корректный номер телефона."));
                }
            }
        }
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // Реализация проверки номера телефона
        // Возможно, вы хотите использовать регулярное выражение или другой метод для проверки
        // Пример: return phoneNumber.matches("^[0-9]{10}$"); // Проверка на 10 цифр
        return true; // Заглушка, всегда возвращаем true
    }
}

*/
