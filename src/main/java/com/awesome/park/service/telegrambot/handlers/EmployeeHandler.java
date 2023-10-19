package com.awesome.park.service.telegrambot.handlers;

import com.awesome.park.entity.Employee;
import com.awesome.park.entity.TelegramInfo;
import com.awesome.park.service.EmployeeService;
import com.awesome.park.service.TelegramInfoService;
import com.awesome.park.service.telegrambot.UserBotDataStorage;
import com.awesome.park.util.BotState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.awesome.park.service.telegrambot.handlers.BaseBookingHandler.currentUserFirstName;
import static com.awesome.park.service.telegrambot.handlers.BaseBookingHandler.currentUserLastName;

@Component
@RequiredArgsConstructor
public class EmployeeHandler {
    private final TelegramInfoService telegramInfoService;
    private final UserBotDataStorage userBotDataStorage;
    private final EmployeeService employeeService;
    private String employeeRole;

    public SendMessage createEmployeeTgInfo(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        String role = callbackQuery.getData();
        Long chatId = callbackQuery.getFrom().getId();

        if (role.equals("role_administrator")) {
            employeeRole = "Администратор";
        } else if (role.equals("role_operator")) {
            employeeRole = "Оператор";
        }

        userBotDataStorage.getUsersBotStates().put(callbackQuery.getMessage().getChatId(), BotState.EMPLOYEE_WAIT_FOR_NAME_AND_SURNAME);

        return new SendMessage(chatId.toString(), "Коллега, введи имя и фамилию");
    }


    public SendMessage saveEmployeeAndTgInfo(Update update) {
        Long chatId = update.getMessage().getChatId();

        TelegramInfo telegramInfo = new TelegramInfo();
        telegramInfo.setChatId(chatId);
        telegramInfo.setUsername(update.getMessage().getFrom().getUserName());
        telegramInfoService.createOrUpdateTelegramInfo(telegramInfo);

        Employee employee = new Employee();
        employee.setFirstName(currentUserFirstName);
        employee.setLastName(currentUserLastName);
        employee.setRole(employeeRole);
        employee.setTelegramInfo(telegramInfo);
        employeeService.saveInDataBase(employee);

        userBotDataStorage.getUsersBotStates().put(chatId, BotState.STOP_BOT);
        return new SendMessage(chatId.toString(), "Сохранил тебя в базу");
    }
}
