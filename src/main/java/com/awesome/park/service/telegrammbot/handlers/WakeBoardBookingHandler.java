package com.awesome.park.service.telegrammbot.handlers;

import com.awesome.park.entity.Customer;
import com.awesome.park.entity.TelegramInfo;
import com.awesome.park.service.BookingService;
import com.awesome.park.service.CustomerService;
import com.awesome.park.service.TelegramInfoService;
import com.awesome.park.service.telegrammbot.UserBotDataStorage;
import com.awesome.park.util.BotState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.awesome.park.util.ValidationUtils.isValidNameAndSurname;
import static com.awesome.park.util.ValidationUtils.isValidPhoneNumber;

@Component
@RequiredArgsConstructor
public class WakeBoardBookingHandler {

    private final BookingService bookingService;

    private final TelegramInfoService telegramInfoService;
    private final UserBotDataStorage userBotDataStorage;
    private final CustomerService customerService;
    private final Customer customer = new Customer();
    private String userName;
    private String currentUserName;
    private Customer findedCustomer;


    public SendMessage handleWakeBoardBooking(Update update, Long chatId) {
        CallbackQuery callbackQuery = update.getCallbackQuery();

        //получим никнейм юзера из телеги
        userName = callbackQuery.getFrom().getUserName();

        // Попробуем найти пользователя по userName (нику в телеге)
        TelegramInfo user = telegramInfoService.findByUsername(userName);
        String response;
        if (user != null) {
            // Пользователь найден, предложим выбрать дату и время бронирования
            findedCustomer = customerService.getCustomerByTelegramInfoId(user.getId());
            response = "Привет, " + findedCustomer.getFirstName() + "! Вы выбрали катание на вейк-борде. Пожалуйста, уточните дату и время бронирования.";
            userBotDataStorage.getUsersBotStates().put(callbackQuery.getMessage().getChatId(), BotState.WAIT_FOR_CONFIRMATION);
            return buildBookingTimeButtonMenu(chatId);
        } else {
            // Пользователь не найден, создаем нового
            response = "Прежде чем записаться и катать давай познакомимся! Введи пожалуйста свое Имя и Фамилию";
            userBotDataStorage.getUsersBotStates().put(callbackQuery.getMessage().getChatId(), BotState.WAIT_FOR_NAME_AND_SURNAME);

        }
        return new SendMessage(chatId.toString(), response);
    }


    public SendMessage checkNameAndSurname(Update update) {
        String messageText = update.getMessage().getText();
        String chatId = update.getMessage().getChatId().toString();

        // Разделим текст сообщения по пробелу
        String[] nameAndSurname = messageText.split(" ");

        // Проверим, что массив имеет длину 2
        if (nameAndSurname.length == 2) {
            String firstName = nameAndSurname[0];
            String lastName = nameAndSurname[1];

            // Проверим валидность имени и фамилии, тут статические методы проверки
            if (!isValidNameAndSurname(firstName, lastName)) {
                return new SendMessage(chatId, "Пожалуйста, введи корректные имя и фамилию. Точно так же как в примере: Роман Кацапов");
            } else {
                // Заполняем нашего пользака
                customer.setFirstName(firstName);
                customer.setLastName(lastName);
                // если текущий пользак новый, ему нужно задать имя
                currentUserName = firstName;
                // Меняем состояние
                userBotDataStorage.getUsersBotStates().put(update.getMessage().getChatId(), BotState.WAIT_FOR_PHONE);

                // Отправляем пользователю подтверждение
                return new SendMessage(chatId, "Круть, рад знакомству с тобой, " + firstName + " " + lastName + "! Теперь введи пожалуйста свой номер телефона в формате +7XXXXXXXXXX");
            }
        } else {
            // Если длина массива не равна 2, отправим сообщение об ошибке
            return new SendMessage(chatId, "Пожалуйста, введи имя и фамилию через пробел. Точно так же как в примере: Роман Кацапов");
        }
    }


    public SendMessage checkPhone(Update update, Long chatId) {
        String phoneNumber = update.getMessage().getText();
        String id = chatId.toString();
        // Проверим, что введен корректный номер телефона
        if (!isValidPhoneNumber(phoneNumber)) {
            // Если номер телефона некорректен, отправим пользователю сообщение об ошибке
            return new SendMessage(id, "Некорректный формат номера телефона. Пожалуйста, введи номер в формате +7XXXXXXXXXX.");
        }
        // Обновляем Сохраняем объект Customer с номером телефона
        saveInDataBase(update, phoneNumber);
        //меняем состояние
        userBotDataStorage.getUsersBotStates().put(update.getMessage().getChatId(), BotState.WAIT_FOR_BOOKING_TIME);
        // Сразу херачим пользователю выбор времени

        return buildBookingTimeButtonMenu(chatId);
    }

    public SendMessage buildBookingTimeButtonMenu(Long chatId) {
        // проверяем если текущий пользователь это уже записанный пользователь, то указываем его как найденного
        if (currentUserName == null) {
            currentUserName = findedCustomer.getFirstName();
        }
        List<LocalDateTime> availableBookingTimes = bookingService.getAvailableBookingTimes();
        InlineKeyboardMarkup keyboard = createInlineKeyboard(availableBookingTimes);
        return SendMessage.builder()
                .chatId(chatId)
                .text("Эгееей, " + currentUserName + "! Скорее выбирай время каталки:").parseMode("Markdown")
                .replyMarkup(keyboard)
                .build();
    }

    public SendMessage checkConfirmation(Update update) {

        return SendMessage.builder()
                .chatId(update.getMessage().getChatId()).text("Дождись подвтерждения:")
                .parseMode("Markdown")
                .build();
    }

    private InlineKeyboardMarkup createInlineKeyboard(List<LocalDateTime> availableTimes) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();
        List<InlineKeyboardButton> currentRow = new ArrayList<>();

        for (LocalDateTime time : availableTimes) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            String buttonText = time.format(DateTimeFormatter.ofPattern("HH:mm"));
            button.setText(buttonText);
            button.setCallbackData(time.toString());

            currentRow.add(button);

            // Если текущий ряд достиг максимальной длины (3 кнопки), добавляем его и создаем новый ряд
            if (currentRow.size() == 3) {
                keyboardRows.add(currentRow);
                currentRow = new ArrayList<>();
            }
        }

        // Добавляем последний ряд, если в нем остались кнопки
        if (!currentRow.isEmpty()) {
            keyboardRows.add(currentRow);
        }

        keyboard.setKeyboard(keyboardRows);
        return keyboard;
    }

    private void saveInDataBase(Update update, String phoneNumber) {
        customer.setPhoneNumber(phoneNumber);
        TelegramInfo telegramInfo = new TelegramInfo();
        telegramInfo.setChatId(update.getMessage().getChatId());
        telegramInfo.setUsername(userName);
        telegramInfoService.createOrUpdateTelegramInfo(telegramInfo);
        customer.setTelegramInfo(telegramInfo);
        customerService.createOrUpdateCustomer(customer);

    }

}
