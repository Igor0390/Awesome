/*
package com.awesome.park.service.telegrammbot;

import com.awesome.park.service.CustomerService;
import com.awesome.park.util.BotMessages;
import com.awesome.park.util.BotState;
import com.awesome.park.util.ConfirmationState;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BotServiceHandlerImpl implements BotServiceHandler {

    private final CustomerService customerService;
    private final UserBotDataStorage userBotDataStorage;

    public SendMessage handleStartCommand(String chatId) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(BotMessages.MESSAGES.enterPhoneMessage())
                .build();
    }

    public SendMessage handlePhoneNumberInput(Message message, String chatId, String phoneNumber, String name) {
        phoneNumber = String.valueOf(message.getText());
        if (isValid(phoneNumber)) {
            userBotDataStorage.getUsersPhoneNumbers().put(message.getFrom().getId(), phoneNumber);
            List<LocalTime> availableStartTimes = customerService.getAvailableStartTimes();

            // Создание клавиатуры с кнопками для выбора времени начала слота
            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
            keyboardMarkup.setResizeKeyboard(true);
            List<KeyboardRow> keyboard = availableStartTimes.stream()
                    .map(startTime -> {
                        KeyboardRow row = new KeyboardRow();
                        row.add(new KeyboardButton(startTime.toString()));
                        return row;
                    })
                    .collect(Collectors.toList());

            keyboardMarkup.setKeyboard(keyboard);
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chatId)
                    .text(BotMessages.MESSAGES.selectTimeMessage())
                    .replyMarkup(keyboardMarkup)
                    .replyToMessageId(message.getMessageId())
                    .build();
            userBotDataStorage.getUsersBotStates().put(message.getFrom().getId(), BotState.WAIT_FOR_BOOKING_TIME);
            return sendMessage;

        } else {
            return SendMessage
                    .builder()
                    .chatId(chatId)
                    .text(BotMessages.MESSAGES.invalidPhoneMessage())
                    .build();
        }
    }

    public SendMessage handleBookingTimeInput(Message message, String chatId, String phoneNumber, String name) {
        Instant instant = getInstant(message);
        return saveToDatabase(message, chatId, phoneNumber, name, instant);
    }

    public Instant getInstant(Message message) {
        String selectedTimeText = message.getText();
        LocalTime selectedTime = LocalTime.parse(selectedTimeText);
        return selectedTime.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant();
    }

    private SendMessage saveToDatabase(Message message, String chatId, String phoneNumber, String name, Instant instant) {
        ResponseEntity<String> response = customerService.createOrUpdateBooking(phoneNumber, name, instant);
        String responseMessage = response.getBody();
        assert responseMessage != null;

        return SendMessage.builder()
                .chatId(chatId)
                .text(responseMessage)
                .replyToMessageId(message.getMessageId())
                .build();
    }

    public SendMessage sendConfirmationRequest(String chatId) {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true);
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("Да"));
        keyboardRow.add(new KeyboardButton("Нет"));
        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(keyboardRow);
        keyboardMarkup.setKeyboard(keyboard);

        return SendMessage.builder()
                .chatId(chatId)
                .text("Вы точно хотите изменить время записи?")
                .replyMarkup(keyboardMarkup)
                .build();
    }

    */
/*    public SendMessage handleConfirmationResponse(String chatId, Message message) {
            long userId = message.getFrom().getId();
            ConfirmationState confirmationState = userBotDataStorage.getConfirmationStates().getOrDefault(userId, ConfirmationState.NONE);

            String confirmationMessage;
            if (confirmationState == ConfirmationState.YES) {
                String phoneNumber = userBotDataStorage.getUsersPhoneNumbers().getOrDefault(userId, "");
                String name = "Telegram: " + message.getFrom().getFirstName() + " " + message.getFrom().getLastName();
                // Здесь должны быть вызовы методов вашего BookingService для изменения времени записи и сохранения в базе данных
                saveToDatabase(message, chatId, phoneNumber, name, getInstant(message));
                confirmationMessage = "Вы изменили время записи.";
            } else if (confirmationState == ConfirmationState.NO) {
                confirmationMessage = "Время записи осталось прежним, спасибо что остались верны своему выбору.";
            } else {
                // Если состояние подтверждения не определено, предоставьте пользователю выбор "Да" или "Нет"
                return sendConfirmationRequest(chatId);
            }

            // После обработки подтверждения, обновите состояние пользователя
            userBotDataStorage.getUsersBotStates().put(userId, BotState.WAIT_FOR_BOOKING_TIME);

            return SendMessage.builder()
                    .chatId(chatId)
                    .text(confirmationMessage)
                    .build();
        }*//*

    public String getSecondTime(Message message) {
        return message.getText();
    }

    public SendMessage handleConfirmationResponse(String chatId, Message message, String timeOfSecondCall) {
        String responseText = message.getText().toLowerCase();
        ConfirmationState confirmationState = ConfirmationState.NONE;

        if (responseText.equals("да")) {
            confirmationState = ConfirmationState.YES;
        } else if (responseText.equals("нет")) {
            confirmationState = ConfirmationState.NO;
        }

        long userId = message.getFrom().getId();
        userBotDataStorage.getConfirmationStates().put(userId, confirmationState);

        String confirmationMessage;
        if (confirmationState == ConfirmationState.YES) {
            // Ваша логика изменения времени записи и сохранения в базе данных
            String phoneNumber = userBotDataStorage.getUsersPhoneNumbers().getOrDefault(userId, "");
            String name = "Telegram: " + message.getFrom().getFirstName() + " " + message.getFrom().getLastName();
            // Здесь должны быть вызовы методов вашего BookingService для изменения времени записи и сохранения в базе данных
//            saveToDatabase(message, chatId, "89527922799", name, );
            customerService.createOrUpdateBooking(phoneNumber, name, Instant.parse(timeOfSecondCall));
            confirmationMessage = "Вы изменили время записи.";
        } else if (confirmationState == ConfirmationState.NO) {

            confirmationMessage = "Время записи осталось прежним! Спасибо, что остались верны своему выбору.";
        } else {
            // Если состояние подтверждения не определено, предоставьте пользователю выбор "Да" или "Нет"
            return sendConfirmationRequest(chatId);
        }

        return SendMessage.builder()
                .chatId(chatId)
                .text(confirmationMessage)
                .build();
    }


    private boolean isValid(String phoneNumber) {
        String regex = "^(\\+7|8)\\d{10}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
}
*/
