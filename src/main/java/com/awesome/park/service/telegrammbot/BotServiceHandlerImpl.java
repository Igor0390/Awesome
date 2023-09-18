package com.awesome.park.service.telegrammbot;


import com.awesome.park.dto.BaseDto;
import com.awesome.park.service.BookingService;
import com.awesome.park.util.BotState;
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
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.awesome.park.util.BotState.START;

/*
@Service
@RequiredArgsConstructor
public class BotServiceHandlerImpl implements BotServiceHandler {

    private final BookingService bookingService;
    private final Map<Long, BotState> usersBotStates = new HashMap<>();
    private final Map<Long, String> usersPhoneNumbers = new HashMap<>();

    public SendMessage handleStartCommand(String chatId, Message message) {
        String phoneNumber = usersPhoneNumbers.getOrDefault(message.getFrom().getId(), "");
        botState(message);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("Введите ваш номер телефона:");

            return sendMessage;

    }

    public BotState botState(Message message){
        return usersBotStates.getOrDefault(message.getFrom().getId(), START);

    }

    public SendMessage handlePhoneNumberInput(Message message, String chatId, String phoneNumber, String name) {
        phoneNumber = String.valueOf(message.getText());
        if (isValid(phoneNumber)) {
            usersPhoneNumbers.put(message.getFrom().getId(), phoneNumber);
            BaseDto dto = new BaseDto();
            dto.setUuid(UUID.randomUUID().toString());
            dto.setPhone(phoneNumber);
            dto.setName(name);

            List<LocalTime> availableStartTimes = bookingService.getAvailableStartTimes();

            // Создание клавиатуры с кнопками для выбора времени начала слота
            ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
            keyboardMarkup.setResizeKeyboard(true);
            List<KeyboardRow> keyboard = new ArrayList<>();
            for (LocalTime startTime : availableStartTimes) {
                KeyboardRow row = new KeyboardRow();
                row.add(new KeyboardButton(startTime.toString()));
                keyboard.add(row);
            }
            keyboardMarkup.setKeyboard(keyboard);
            SendMessage sendMessage = SendMessage.builder()
                    .chatId(chatId)
                    .text("Выберите время начала вашей каталки:")
                    .replyMarkup(keyboardMarkup)
                    .replyToMessageId(message.getMessageId())
                    .build();

            usersBotStates.put(message.getFrom().getId(), BotState.WAIT_FOR_BOOKING_TIME);
            return sendMessage;
        } else {
            return SendMessage
                    .builder()
                    .chatId(chatId)
                    .text("Ввели некоректные данные! Попробуйте еще раз! " +
                            "введите номер телефона в формате  +7********** или 8**********")
                    .build();
        }


    }

    public SendMessage handleBookingTimeInput(Message message, String chatId, String phoneNumber, String name) {
        String selectedTimeText = message.getText();
        LocalTime selectedTime = LocalTime.parse(selectedTimeText);
        Instant instant = selectedTime.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant();
        ResponseEntity<String> response = bookingService.createOrUpdateBooking(phoneNumber, name, instant);
        String responseMessage = response.getBody();
        assert responseMessage != null;

        return SendMessage.builder()
                .chatId(chatId)
                .text(responseMessage)
                .replyToMessageId(message.getMessageId())
                .build();
    }

    public boolean isValid(String phoneNumber) {
        String regex = "^(\\+7|8)\\d{10}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }



}

*/