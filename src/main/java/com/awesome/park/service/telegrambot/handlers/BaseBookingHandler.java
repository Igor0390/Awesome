package com.awesome.park.service.telegrambot.handlers;

import com.awesome.park.entity.Customer;
import com.awesome.park.entity.TelegramInfo;
import com.awesome.park.service.CustomerService;
import com.awesome.park.service.TelegramInfoService;
import com.awesome.park.service.telegrambot.InlineButtonKeyboard;
import com.awesome.park.service.telegrambot.UserBotDataStorage;
import com.awesome.park.util.BotState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import javax.transaction.Transactional;

import static com.awesome.park.util.ValidationUtils.isValidNameAndSurname;
import static com.awesome.park.util.ValidationUtils.isValidPhoneNumber;

@Component
@RequiredArgsConstructor
public class BaseBookingHandler {
    private final TelegramInfoService telegramInfoService;
    private final CustomerService customerService;
    private final UserBotDataStorage userBotDataStorage;
    private final WakeBoardHandler wakeBoardHandler;
    private final SupBoardHandler supBoardHandler;
    private final InlineButtonKeyboard buttonKeyboard;

    static Customer foundCustomer;
    static String currentUserFirstName;
    static String currentUserLastName;
    static String telegramUserName;
    private String currentUserPhoneNumber;

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
                // если текущий пользак новый, ему нужно задать имя и фамилию
                currentUserFirstName = firstName;
                currentUserLastName = lastName;
                // Меняем состояние
                userBotDataStorage.getUsersBotStates().put(update.getMessage().getChatId(), botState);
                // Отправляем пользователю подтверждение
                String welcomeMessage = "Круть, рад знакомству с тобой, ";
                if ((botState != BotState.EMPLOYEE_WAIT_FOR_SAVE)) {
                    return new SendMessage(chatId, welcomeMessage
                            + firstName
                            + "! Теперь введи пожалуйста свой номер телефона в формате +7XXXXXXXXXX");
                } else {
                    return SendMessage.builder()
                            .chatId(chatId)
                            .text(welcomeMessage
                                    + firstName
                                    + " Надеюсь у тебя все получится!")
                            .replyMarkup(buttonKeyboard.getReplyKeyboardMarkup("По любасу!", "Заебись!"))
                            .parseMode("Markdown")
                            .build();
                }
            }
        } else {
            // Если длина массива не равна 2, отправим сообщение об ошибке
            return new SendMessage(chatId, "Пожалуйста, введи имя и фамилию через пробел. Точно так же как в примере: Роман Кацапов");
        }
    }

    public SendMessage checkPhone(Update update, Long chatId, BotState state) {
        currentUserPhoneNumber = update.getMessage().getText();
        String id = chatId.toString();
        // Проверим, что введен корректный номер телефона
        if (!isValidPhoneNumber(currentUserPhoneNumber)) {
            // Если номер телефона некорректен, отправим пользователю сообщение об ошибке
            return new SendMessage(id, "Некорректный формат номера телефона. Пожалуйста, введи номер в формате +7XXXXXXXXXX.");
        }
        // Обновляем Сохраняем объект Customer с номером телефона
        saveInDataBase(update);
//        updateCustomerData(currentUserFirstName, currentUserLastName, phoneNumber);
        //меняем состояние
        userBotDataStorage.getUsersBotStates().put(update.getMessage().getChatId(), state);
        // Сразу херачим пользователю выбор времени
        if (state == BotState.WAKE_WAIT_FOR_BOOKING_TIME) {
            return wakeBoardHandler.buildBookingTimeButtonMenu(chatId, " наши вейкборды: AWESOME! ", BotState.WAKE_WAIT_FOR_CONFIRMATION);
        } else if (state == BotState.SUP_BOARD_WAIT_FOR_SUP_BOOKING_TIME) {
            return supBoardHandler.buildSupTimeButtonMenu(chatId, " наши сап-борды: AWESOME! ", BotState.SUP_BOARD_WAIT_FOR_CONFIRMATION);
        }
        return new SendMessage(id, "после записи номера телефона, что-то пошло не так");
    }

    @Transactional
    public void saveInDataBase(Update update) {
        TelegramInfo telegramInfo = new TelegramInfo();
        telegramInfo.setChatId(update.getMessage().getChatId());
        telegramInfo.setUsername(telegramUserName);

        telegramInfoService.createOrUpdateTelegramInfo(telegramInfo);
        Customer customer = new Customer();
        customer.setFirstName(currentUserFirstName);
        customer.setLastName(currentUserLastName);
        customer.setPhoneNumber(currentUserPhoneNumber);
        customer.setTelegramInfoId(telegramInfo.getId());

        customerService.createOrUpdateCustomer(customer);
    }
}
