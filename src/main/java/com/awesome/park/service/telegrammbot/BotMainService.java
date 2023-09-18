package com.awesome.park.service.telegrammbot;


import com.awesome.park.config.botconfig.BotConfig;
import com.awesome.park.dto.BaseDto;
import com.awesome.park.service.BookingService;
import com.awesome.park.util.BotState;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
@RequiredArgsConstructor
public class BotMainService extends TelegramLongPollingBot {
    private final BotConfig botConfig;
    private final BookingService bookingService;
//    private final BotServiceHandler botServiceHandler;
    private final Map<Long, BotState> usersBotStates = new HashMap<>();
    private final Map<Long, String> usersPhoneNumbers = new HashMap<>();

    @Override
    public String getBotUsername() {
        return botConfig.name();
    }

    @Override
    public String getBotToken() {
        return botConfig.token();
    }



    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message message = update.getMessage();
            String chatId = message.getChatId().toString();
            String phoneNumber = usersPhoneNumbers.getOrDefault(message.getFrom().getId(), "");
            String name = "Telegram: " + update.getMessage().getFrom().getFirstName() + " " + update.getMessage().getFrom().getLastName();

            BotState currentState = usersBotStates.getOrDefault(message.getFrom().getId(), BotState.START);

            switch (currentState) {
                case START -> {
                    if (message.getText().equalsIgnoreCase("/booking")) {
                        handleStartCommand(chatId);
                        usersBotStates.put(message.getFrom().getId(), BotState.WAIT_FOR_PHONE);
                    }
                }
                case WAIT_FOR_PHONE -> handlePhoneNumberInput(message, chatId, phoneNumber, name);
                case WAIT_FOR_BOOKING_TIME -> handleBookingTimeInput(message, chatId, phoneNumber, name);
            }
        }
    }

   public void handleStartCommand(String chatId) {
        {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText("Введите ваш номер телефона:");

            executeSafely(sendMessage);
        }
    }

    public void handlePhoneNumberInput(Message message, String chatId, String phoneNumber, String name) {
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

            executeSafely(sendMessage);
            usersBotStates.put(message.getFrom().getId(), BotState.WAIT_FOR_BOOKING_TIME);
        } else {
            executeSafely(
                    SendMessage
                            .builder()
                            .chatId(chatId)
                            .text("Ввели некоректные данные! Попробуйте еще раз! " +
                                    "введите номер телефона в формате  +7********** или 8**********")
                            .build());
        }

    }

    public void handleBookingTimeInput(Message message, String chatId, String phoneNumber, String name) {
        String selectedTimeText = message.getText();
        LocalTime selectedTime = LocalTime.parse(selectedTimeText);
        Instant instant = selectedTime.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant();
        ResponseEntity<String> response = bookingService.createOrUpdateBooking(phoneNumber, name, instant);
        String responseMessage = response.getBody();
        assert responseMessage != null;
        SendMessage responseSendMessage = SendMessage.builder()
                .chatId(chatId)
                .text(responseMessage)
                .replyToMessageId(message.getMessageId())
                .build();

        executeSafely(responseSendMessage);
    }

   public static boolean isValid(String phoneNumber) {
        String regex = "^(\\+7|8)\\d{10}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }
    public void executeSafely(SendMessage message) {
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


}


    /* @Override
     public void onUpdateReceived(Update update) {
         if (update.hasMessage() && update.getMessage().hasText()) {
             Message message = update.getMessage();
             String chatId = message.getChatId().toString();
             String phoneNumber = usersPhoneNumbers.getOrDefault(message.getFrom().getId(), "");
             String name = update.getMessage().getFrom().getFirstName() + " " + update.getMessage().getFrom().getLastName();

             BotState currentState = usersBotStates.getOrDefault(message.getFrom().getId(), BotState.START);
             switch (currentState) {
                 case START -> {
                     if (message.getText().equalsIgnoreCase("/booking")) {
                         SendMessage sendMessage = new SendMessage();
                         sendMessage.setChatId(chatId);
                         sendMessage.setText("Введите ваш номер телефона:");


                         try {
                             execute(sendMessage);
                         } catch (TelegramApiException e) {
                             e.printStackTrace();
                         }
                     }
                     usersBotStates.put(message.getFrom().getId(), BotState.WAIT_FOR_PHONE);
                 }

                 case WAIT_FOR_PHONE -> {
                     phoneNumber = String.valueOf(message.getText());
                     if (phoneNumber.startsWith("+7") || phoneNumber.startsWith("8")) {
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
                                 .text("Выберите время начала слота:")
                                 .replyMarkup(keyboardMarkup)
                                 .replyToMessageId(message.getMessageId())
                                 .build();
                         try {
                             execute(sendMessage);
                         } catch (TelegramApiException e) {
                             e.printStackTrace();
                         }
                     }
                     usersBotStates.put(message.getFrom().getId(), BotState.WAIT_FOR_BOOKING_TIME);
                 }
                 case WAIT_FOR_BOOKING_TIME -> {
                     String selectedTimeText = message.getText();
                     LocalTime selectedTime = LocalTime.parse(selectedTimeText);
                     Instant instant = selectedTime.atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant();
                     ResponseEntity<String> response = bookingService.createOrUpdateBooking(phoneNumber, name, instant);
                     String responseMessage = response.getBody();
                     assert responseMessage != null;
                     SendMessage responseSendMessage = SendMessage.builder()
                             .chatId(chatId)
                             .text(responseMessage)
                             .replyToMessageId(message.getMessageId())
                             .build();
                     try {
                         execute(responseSendMessage);
                     } catch (TelegramApiException e) {
                         e.printStackTrace();
                     }
                 }
             }
         }
     }*/


