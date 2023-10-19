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

import static com.awesome.park.service.telegrambot.handlers.BaseBookingHandler.currentUserFirstName;
import static com.awesome.park.service.telegrambot.handlers.BaseBookingHandler.foundCustomer;
import static com.awesome.park.service.telegrambot.handlers.BaseBookingHandler.telegramUserName;
import static com.awesome.park.util.ActivityType.SUP_BOARD;


@Component
@RequiredArgsConstructor
public class SupBoardHandler {
    public static final Integer MAX_CAPACITY = 8;
    private final TelegramInfoService telegramInfoService;
    private final CustomerService customerService;
    private final UserBotDataStorage userBotDataStorage;
    private final BookingService bookingService;
    private final InlineButtonKeyboard buttonKeyboard;


    private LocalDateTime selectedTime;
    private int availableNum;

    public SendMessage handleSupBoardBooking(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        //получим никнейм юзера из телеги
        telegramUserName = callbackQuery.getFrom().getUserName();
        Long chatId = callbackQuery.getFrom().getId();
        // Попробуем найти пользователя по userName (нику в телеге)
        TelegramInfo user = telegramInfoService.findByUsername(telegramUserName);
        String response;
        if (user != null) {
            // Пользователь найден, предложим выбрать дату и время бронирования
            foundCustomer = customerService.getCustomerByTelegramInfoId(user.getId());
            return buildSupTimeButtonMenu(chatId, " дружище !", BotState.SUP_BOARD_WAIT_FOR_CONFIRMATION);
        } else {
            // Пользователь не найден, создаем нового
            response = """
                    Прежде чем записаться и покорять сап-борд, давай познакомимся!

                     Введи пожалуйста свое Имя и Фамилию, точно так же как в этом примере:

                     Роман Кацапов""";
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
        if (currentUserFirstName == null) {
            currentUserFirstName = foundCustomer.getFirstName();
        }
        List<LocalDateTime> availableBookingTimes = bookingService.getAvailableBookingTimesForSupBoards(Duration.ofHours(1), SUP_BOARD.getId(), MAX_CAPACITY, selectedTime);
        InlineKeyboardMarkup keyboard = createInlineKeyboard(availableBookingTimes);

        // Если есть доступные сап-борды, показать кнопку для выбора времени
        userBotDataStorage.getUsersBotStates().put(chatId, botState);
        return SendMessage.builder()
                .chatId(chatId)
                .text("Юхууу, " + foundCustomer.getFirstName() + textMessage + "! Выбирай время старта для катания на сапе:")
                .parseMode("Markdown")
                .replyMarkup(keyboard)
                .build();
    }

    public InlineKeyboardMarkup createSupBoardAmountKeyboard(Long chatId) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();
        int bookedSupBoardsCount = getBookedSupBoardsCountAtTime(selectedTime);

        availableNum = MAX_CAPACITY - bookedSupBoardsCount;

        // Создаем строку с кнопками для количества сап-бордов
        List<InlineKeyboardButton> row = new ArrayList<>();

        // Создаем кнопки для количества сап-бордов от 1 до availableNum
        for (int amount = 1; amount <= availableNum; amount++) {
            row.add(createSupBoardAmountButton(Integer.toString(amount), Integer.toString(amount)));
        }

        keyboardRows.add(row);
        keyboard.setKeyboard(keyboardRows);

        userBotDataStorage.getUsersBotStates().put(chatId, BotState.SUP_BOARD_WAIT_FOR_CONFIRMATION);

        return keyboard;
    }

    public SendMessage createSupBoardKeyboard(Long chatId, String callbackData) {
        if (currentUserFirstName == null) {
            currentUserFirstName = foundCustomer.getFirstName();
        }
        String timeSlot = callbackData.replace("SUP_BOARD_TIME_SLOT:", "");
        selectedTime = LocalDateTime.parse(timeSlot);
        InlineKeyboardMarkup amountKeyboard = createSupBoardAmountKeyboard(chatId);
        String message = currentUserFirstName
                + ", приедешь грести на сапе в: "
                + selectedTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                + ". А сколько тебе нужно сап-бордов? Выбери из доступного количества:";
        int bookedSupBoardsCount = getBookedSupBoardsCountAtTime(selectedTime);
        if (bookedSupBoardsCount >= MAX_CAPACITY) {
            // Если забронировано максимальное количество сап-бордов, не показывать кнопку
            userBotDataStorage.getUsersBotStates().put(chatId, BotState.SUP_BOARD_WAIT_FOR_CONFIRMATION);

            return SendMessage.builder()
                    .chatId(chatId)
                    .text("Извини, " + currentUserFirstName + " но на это время нет доступных сап-бордов. Выбери другое")
                    .replyMarkup(buttonKeyboard.getReplyKeyboardMarkup("Хорошо", "Откажусь"))
                    .parseMode("Markdown")
                    .build();

        }
        return SendMessage.builder()
                .chatId(chatId)
                .text(message)
                .parseMode("Markdown")
                .replyMarkup(amountKeyboard)
                .build();
    }

    public SendMessage checkConfirmation(Update update) {
        if (update.getMessage() != null) {
            return processConfirmationMessage(update);
        } else {
            return processCallbackConfirmation(update);
        }
    }

    private SendMessage processConfirmationMessage(Update update) {
        String confirmation = update.getMessage().getText().toLowerCase(); // Приведем к нижнему регистру для удобства
        if (confirmation.equals("откажусь")) {
            return processCancellation(update.getMessage().getChatId());
        }

        userBotDataStorage.getUsersBotStates().put(update.getMessage().getChatId(), BotState.SUP_BOARD_WAIT_FOR_SUP_BOOKING_TIME);
        return processCallbackConfirmation(update);
    }

    private SendMessage processCallbackConfirmation(Update update) {

        Long chatId;
        int num ;
        if (update.getCallbackQuery() != null) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
            String data = update.getCallbackQuery().getData();
            String countOfSupBoards = data.replace("BOARD_COUNT:", "");
            num = Integer.parseInt(countOfSupBoards);
        } else {
            chatId = update.getMessage().getChatId();
            num = MAX_CAPACITY;
        }

        TelegramInfo user = telegramInfoService.findByUsername(telegramUserName);
        Long customerId = customerService.getCustomerByTelegramInfoId(user.getId()).getId();

        if (num > 0 && num <= availableNum) {
            bookSupBoards(num, customerId);
            return createSuccessMessage(chatId, num);
        } else if (num <= 0) {
            return new SendMessage(chatId.toString(), "Пожалуйста, введи количество сап-бордов больше 0.");
        } else {
            return createErrorMessage(chatId);
        }
    }

    private SendMessage processCancellation(Long chatId) {
        userBotDataStorage.getUsersBotStates().put(chatId, BotState.STOP_BOT);
        return new SendMessage(chatId.toString(), "Ок. Приезжай в другой раз");
    }

    private void bookSupBoards(int num, Long customerId) {
            Booking booking = new Booking();
            booking.setActivityId(SUP_BOARD.getId());
            booking.setCustomerId(customerId);
            booking.setBookingTime(selectedTime);
            booking.setActivityCount(num);
            bookingService.createOrUpdateSupBoardBooking(booking);

    }

    private SendMessage createSuccessMessage(Long chatId, int num) {
        String successMessage = "Поздравляю, "
                + currentUserFirstName
                + "  тобой было забронировано "
                + num + " "
                + getSupBoardMessage(num)
                + ". Приятного катания!";
        userBotDataStorage.getUsersBotStates().put(chatId, BotState.STOP_BOT);
        return new SendMessage(chatId.toString(), successMessage);
    }

    private SendMessage createErrorMessage(Long chatId) {
        userBotDataStorage.getUsersBotStates().put(chatId, BotState.SUP_BOARD_WAIT_FOR_CONFIRMATION);
        return new SendMessage(chatId.toString(), """
                Ага я тебя понял, но на это время по прежнему нет доступных сапов.\s

                Пожалуйста, выбери другое время из таблички выше""");
    }

    public int getBookedSupBoardsCountAtTime(LocalDateTime selectedTime) {
        return bookingService.getSupBoardsCountAtTime(SUP_BOARD.getId(), selectedTime);
    }

    public InlineKeyboardButton createSupBoardAmountButton(String text, String callbackData) {
        InlineKeyboardButton button = new InlineKeyboardButton();
        button.setText(text);
        button.setCallbackData("BOARD_COUNT:" + callbackData);
        return button;
    }

    public String getSupBoardMessage(int num) {
        return switch (num) {
            case 1 -> "сап-борд";
            case 2, 3, 4 -> "сап-борда";
            default -> "сап-бордов";
        };
    }
}
