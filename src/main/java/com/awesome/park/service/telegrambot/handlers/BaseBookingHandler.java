package com.awesome.park.service.telegrambot.handlers;

import com.awesome.park.entity.Customer;
import com.awesome.park.entity.TelegramInfo;
import com.awesome.park.service.CustomerService;
import com.awesome.park.service.TelegramInfoService;
import com.awesome.park.service.telegrambot.UserBotDataStorage;
import com.awesome.park.util.BotState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.awesome.park.util.ValidationUtils.isValidNameAndSurname;
import static com.awesome.park.util.ValidationUtils.isValidPhoneNumber;

@Component
@RequiredArgsConstructor
public class BaseBookingHandler {
    private final TelegramInfoService telegramInfoService;
    private final CustomerService customerService;
    private final UserBotDataStorage userBotDataStorage;
    private final WakeBoardBookingHandler wakeBoardBookingHandler;
    private final SupBoardHandler supBoardHandler;
    private final Customer customer = new Customer();
    static Customer findedCustomer;
    static String currentUserName;
    static String telegramUserName;

    public SendMessage checkNameAndSurname(Update update, BotState botState) {
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
                userBotDataStorage.getUsersBotStates().put(update.getMessage().getChatId(), botState);
                // Отправляем пользователю подтверждение
                return new SendMessage(chatId, "Круть, рад знакомству с тобой, " + firstName + " " + lastName + "! Теперь введи пожалуйста свой номер телефона в формате +7XXXXXXXXXX");
            }
        } else {
            // Если длина массива не равна 2, отправим сообщение об ошибке
            return new SendMessage(chatId, "Пожалуйста, введи имя и фамилию через пробел. Точно так же как в примере: Роман Кацапов");
        }
    }

    public SendMessage checkPhone(Update update, Long chatId, BotState botState) {

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
        userBotDataStorage.getUsersBotStates().put(update.getMessage().getChatId(), botState);
        // Сразу херачим пользователю выбор времени
        if (botState == BotState.WAKE_WAIT_FOR_BOOKING_TIME) {
            return wakeBoardBookingHandler.buildBookingTimeButtonMenu(chatId, " это будет AWESOME! ", BotState.WAKE_WAIT_FOR_CONFIRMATION);
        } else if (botState == BotState.SUP_BOARD_WAIT_FOR_SUP_BOOKING_TIME) {
            return supBoardHandler.buildSupTimeButtonMenu(chatId, " SUP это будет AWESOME! ", BotState.SUP_BOARD_WAIT_FOR_CONFIRMATION);
        }
        return new SendMessage(id, "после записи номера телефона, что-то пошло не так");
    }


    private void saveInDataBase(Update update, String phoneNumber) {
        customer.setPhoneNumber(phoneNumber);
        TelegramInfo telegramInfo = new TelegramInfo();
        telegramInfo.setChatId(update.getMessage().getChatId());
        telegramInfo.setUsername(telegramUserName);
        telegramInfoService.createOrUpdateTelegramInfo(telegramInfo);
        customer.setTelegramInfo(telegramInfo);
        customerService.createOrUpdateCustomer(customer);
    }
}
