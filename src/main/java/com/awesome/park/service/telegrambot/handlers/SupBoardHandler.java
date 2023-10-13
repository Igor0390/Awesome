package com.awesome.park.service.telegrambot.handlers;

import com.awesome.park.entity.Booking;
import com.awesome.park.entity.TelegramInfo;
import com.awesome.park.service.BookingService;
import com.awesome.park.service.CustomerService;
import com.awesome.park.service.TelegramInfoService;
import com.awesome.park.service.telegrambot.InlineButtonKeyboard;
import com.awesome.park.service.telegrambot.UserBotDataStorage;
import com.awesome.park.util.BotState;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.awesome.park.service.telegrambot.handlers.BaseBookingHandler.currentUserName;
import static com.awesome.park.service.telegrambot.handlers.BaseBookingHandler.foundCustomer;
import static com.awesome.park.service.telegrambot.handlers.BaseBookingHandler.telegramUserName;
import static com.awesome.park.util.ActivityType.SUP_BOARD;


@Component
@RequiredArgsConstructor
public class SupBoardHandler {
    private final TelegramInfoService telegramInfoService;
    private final CustomerService customerService;
    private final UserBotDataStorage userBotDataStorage;
    private final InlineButtonKeyboard inlineButtonKeyboard;
    private final BookingService bookingService;

    private LocalDateTime selectedTime;

    public SendMessage handleSupBoardBooking(Update update, Long chatId) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        //получим никнейм юзера из телеги
        telegramUserName = callbackQuery.getFrom().getUserName();
        // Попробуем найти пользователя по userName (нику в телеге)
        TelegramInfo user = telegramInfoService.findByUsername(telegramUserName);
        String response;
        if (user != null) {
            // Пользователь найден, предложим выбрать дату и время бронирования
            foundCustomer = customerService.getCustomerByTelegramInfoId(user.getId());
            return buildSupTimeButtonMenu(chatId, " дружище я тебя помню ! ", BotState.SUP_BOARD_WAIT_FOR_CONFIRMATION);
        } else {
            // Пользователь не найден, создаем нового
            response =  "Прежде чем записаться и покорять сап-борд, давай познакомимся! Введи пожалуйста свое Имя и Фамилию, точно так же как в этом примере: Роман Кацапов";
            userBotDataStorage.getUsersBotStates().put(callbackQuery.getMessage().getChatId(), BotState.SUP_BOARD_WAIT_FOR_NAME_AND_SURNAME);

        }
        return new SendMessage(chatId.toString(), response);
    }

    public InlineKeyboardMarkup createInlineKeyboard(List<LocalDateTime> availableTimes) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();
        List<InlineKeyboardButton> currentRow = new ArrayList<>();

        for (LocalDateTime time : availableTimes) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            String buttonText = time.format(DateTimeFormatter.ofPattern("HH:mm"));
            button.setText(buttonText);
            button.setCallbackData("SUP_BOARD_TIME_SLOT:" + time);

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

    public SendMessage buildSupTimeButtonMenu(Long chatId, String textMessage, BotState botState) {
        // проверяем если текущий пользователь это уже записанный пользователь, то указываем его как найденного
        if (currentUserName == null) {
            currentUserName = foundCustomer.getFirstName();
        }
        List<LocalDateTime> availableBookingTimes = bookingService.getAvailableBookingTimes(Duration.ofHours(1), SUP_BOARD.getId());
        userBotDataStorage.getUsersBotStates().put(chatId, botState);
        InlineKeyboardMarkup keyboard = createInlineKeyboard(availableBookingTimes);

        return SendMessage.builder()
                .chatId(chatId)
                .text("Юхууу, "
                        + currentUserName
                        + textMessage
                        + "! Выбирай время старта для катания на сапе:")
                .parseMode("Markdown")
                .replyMarkup(keyboard)
                .build();
    }

    public SendMessage createSupBoardKeyboard(Long chatId, String callbackData) {
        if (currentUserName == null) {
            currentUserName = foundCustomer.getFirstName();
        }
        String timeSlot = callbackData.replace("SUP_BOARD_TIME_SLOT:", "");
        selectedTime = LocalDateTime.parse(timeSlot);
        InlineKeyboardMarkup amountKeyboard = inlineButtonKeyboard.createSupBoardAmountKeyboard(chatId);
        String message = currentUserName
                + ", ты выбрал время: "
                + selectedTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                + ". А сколько тебе нужно сап-бордов?";
        return SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .parseMode("Markdown")
                .replyMarkup(amountKeyboard)
                .build();
    }

    public SendMessage checkConfirmation(Update update) {
        Long chatId = update.getCallbackQuery().getMessage().getChatId();
        String data = update.getCallbackQuery().getData();
        String countOfSupBoards = data.replace("BOARD_COUNT:", "");
        int num = Integer.parseInt(countOfSupBoards);

        TelegramInfo user = telegramInfoService.findByUsername(telegramUserName);
        Long customerId = customerService.getCustomerByTelegramInfoId(user.getId()).getId();

        // Получить количество забронированных сап-бордов в выбранное время
        int bookedSupBoardsCount = getBookedSupBoardsCountAtTime(selectedTime);

        int availableNum = 8 - bookedSupBoardsCount;
        if (num > 0 && num <= availableNum) {
            for (int i = 0; i < num; i++) {
                Booking booking = new Booking();
                booking.setActivityId(SUP_BOARD.getId());
                booking.setCustomerId(customerId);
                booking.setBookingTime(selectedTime);
                bookingService.createOrUpdateSupBoardBooking(booking);
            }

            String successMessage = "Поздравляю, " + currentUserName+ "  тобой было забронировано " + num + " " + getSupBoardMessage(num) + ". Приятного катания!";
            userBotDataStorage.getUsersBotStates().put(chatId, BotState.STOP_BOT);
            return new SendMessage(chatId.toString(), successMessage);
        } else if (num <= 0) {
            return new SendMessage(chatId.toString(), "Пожалуйста, введи количество сап-бордов больше 0.");
        } else {
            userBotDataStorage.getUsersBotStates().put(update.getMessage().getChatId(), BotState.SUP_BOARD_WAIT_FOR_CONFIRMATION);
            return new SendMessage(chatId.toString(), "Ухх соррян, но на это время доступно только " + availableNum + getSupBoardMessage(num) + ".Пожалуйста, выбери меньшее количество.");

        }
    }

    public int getBookedSupBoardsCountAtTime(LocalDateTime selectedTime) {
        return bookingService.getSupBoardsCountAtTime(SUP_BOARD.getId(), selectedTime);
    }

    public String getSupBoardMessage(int num) {
        if (num >= 1 && num <= 4) {
            return "сап-борда";
        } else {
            return "сап-бордов";
        }
    }
}
