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
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.awesome.park.service.telegrambot.handlers.BaseBookingHandler.currentUserFirstName;
import static com.awesome.park.service.telegrambot.handlers.BaseBookingHandler.foundCustomer;
import static com.awesome.park.service.telegrambot.handlers.BaseBookingHandler.telegramUserName;
import static com.awesome.park.util.ActivityType.WAKE_BOARD;

@Component
@RequiredArgsConstructor
public class WakeBoardHandler {

    private final BookingService bookingService;

    private final TelegramInfoService telegramInfoService;
    private final UserBotDataStorage userBotDataStorage;
    private final CustomerService customerService;
    private final InlineButtonKeyboard buttonKeyboard;

    private LocalDateTime selectedTime;


    public SendMessage handleWakeBoardBooking(Update update) {
        CallbackQuery callbackQuery = update.getCallbackQuery();
        Long chatId = callbackQuery.getFrom().getId();
        //получим никнейм юзера из телеги
        telegramUserName = callbackQuery.getFrom().getUserName();
        // Попробуем найти пользователя по userName (нику в телеге)
        TelegramInfo user = telegramInfoService.findByUsername(telegramUserName);
        String response;
        if (user != null) {
            // Пользователь найден, предложим выбрать дату и время бронирования
            foundCustomer = customerService.getCustomerByTelegramInfoId(user.getId());
            return buildBookingTimeButtonMenu(chatId, " дружище я тебя помню ! ", BotState.WAKE_WAIT_FOR_CONFIRMATION);
        } else {
            // Пользователь не найден, создаем нового
            response = """
                    Прежде чем записаться и катать на вейке давай познакомимся!

                     Введи пожалуйста свои Имя и Фамилию точно так же как в этом примере:

                     Роман Кацапов""";
            userBotDataStorage.getUsersBotStates().put(callbackQuery.getMessage().getChatId(), BotState.WAKE_WAIT_FOR_NAME_AND_SURNAME);

        }
        return new SendMessage(chatId.toString(), response);
    }

    public SendMessage buildBookingTimeButtonMenu(Long chatId, String text, BotState botState) {
        // проверяем если текущий пользователь это уже записанный пользователь, то указываем его как найденного
        if (currentUserFirstName == null) {
            currentUserFirstName = foundCustomer.getFirstName();
        }
        List<LocalDateTime> availableBookingTimes = getAvailableBookingTimes();
        InlineKeyboardMarkup keyboard = createInlineKeyboard(availableBookingTimes);
        userBotDataStorage.getUsersBotStates().put(chatId, botState);
        return SendMessage.builder()
                .chatId(chatId)
                .text("Эгееей, " + currentUserFirstName + text + "! Скорее выбирай время каталки:")
                .parseMode("Markdown")
                .replyMarkup(keyboard).build();
    }

    public InlineKeyboardMarkup createInlineKeyboard(List<LocalDateTime> availableTimes) {
        InlineKeyboardMarkup keyboard = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> keyboardRows = new ArrayList<>();
        List<InlineKeyboardButton> currentRow = new ArrayList<>();

        for (LocalDateTime time : availableTimes) {
            InlineKeyboardButton button = new InlineKeyboardButton();
            String buttonText = time.format(DateTimeFormatter.ofPattern("HH:mm"));
            button.setText(buttonText);
            button.setCallbackData("TIME_SLOT:" + time);

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


    public SendMessage handleTimeSlotCallback(Long chatId, String callbackData) {
        if (currentUserFirstName == null) {
            currentUserFirstName = foundCustomer.getFirstName();
        }
        String timeSlot = callbackData.replace("TIME_SLOT:", "");
        selectedTime = LocalDateTime.parse(timeSlot);
        LocalDateTime currentTime = LocalDateTime.now();
        if (selectedTime.isBefore(currentTime)) {
            // Пользователь выбрал прошедшее время.
            return new SendMessage(chatId.toString(), "Соррян, нельзя бронировать прошедшее время, выбери другое пожалуйста!");
        }
        // Создаем клавиатуру с кнопками "да" и "нет"
        ReplyKeyboardMarkup replyKeyboardMarkup = buttonKeyboard.getReplyKeyboardMarkup("Да", "Нет");
        // Теперь создаем сообщение с клавиатурой
        SendMessage message = new SendMessage(chatId.toString(),
                currentUserFirstName
                        + ", "
                        + selectedTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                        + " - тоже думаю, что это лучшее время для каталки,\n\n"
                        + " записываю?");
        message.setReplyMarkup(replyKeyboardMarkup);

        return message;
    }

    public SendMessage checkConfirmation(Update update) {
        String chatId = update.getMessage().getChatId().toString();
        String confirmation = update.getMessage().getText().toLowerCase(); // Приведем к нижнему регистру для удобства

        if (confirmation.equalsIgnoreCase("да")) {// Пользователь подтвердил запись, создаем бронирование

            BookingByCustomerId bookingInfo = createBookingByTelegramInfo();

            Booking existBooking = bookingService.getByCustomerIdAndActivityId(bookingInfo.customerId(), WAKE_BOARD.getId());
            if (existBooking != null) {
                LocalDateTime chosenBookingTime = existBooking.getBookingTime();
                List<LocalDateTime> availableBookingTimes = getAvailableBookingTimes();
                if (!availableBookingTimes.contains(chosenBookingTime)) { // если доступное время не содержит выбранное
                    stopBot(update); // меняем состояние бота
                    try {
                        return createBookingSuccessMessage(chatId, bookingInfo.booking()); // и пробуем создать бронирование
                    } catch (Exception e) {
                        return new SendMessage(chatId, "Извини, но я не могу записать тебя повторно");
                    }
                }
                stopBot(update);
                return new SendMessage(chatId, "Ой, кажется я тебя уже записывал");
            } else {
                stopBot(update);
                return createBookingSuccessMessage(chatId, bookingInfo.booking());
            }

        } else if (confirmation.equalsIgnoreCase("нет")) {
            // Пользователь отказался от записи, изменяем состояние
            userBotDataStorage.getUsersBotStates().put(update.getMessage().getChatId(), BotState.WAKE_WAIT_FOR_BOOKING_TIME);
            return buildBookingTimeButtonMenu(update.getMessage().getChatId(), " чего стесняешься ???", BotState.WAKE_WAIT_FOR_CONFIRMATION);
        } else {
            stopBot(update);
            return new SendMessage(chatId, "ох...не понимаю тебя...");
        }
    }

    private List<LocalDateTime> getAvailableBookingTimes() {
        return bookingService.getAvailableBookingTimes(Duration.ofMinutes(30), WAKE_BOARD.getId());
    }

    private void stopBot(Update update) {
        userBotDataStorage.getUsersBotStates().put(update.getMessage().getChatId(), BotState.STOP_BOT);
    }

    private BookingByCustomerId createBookingByTelegramInfo() {
        TelegramInfo user = telegramInfoService.findByUsername(telegramUserName);
        Long customerId = customerService.getCustomerByTelegramInfoId(user.getId()).getId();
        Booking booking = new Booking();
        booking.setCustomerId(customerId);
        booking.setActivityId(WAKE_BOARD.getId());
        booking.setBookingTime(selectedTime);
        booking.setActivityCount(1);
        return new BookingByCustomerId(customerId, booking);
    }
    private record BookingByCustomerId(Long customerId, Booking booking) {
    }

    private SendMessage createBookingSuccessMessage(String chatId, Booking booking) {
        bookingService.createOrUpdateBookingAndCustomer(booking); // Сохраняем в базе данных
        return new SendMessage(chatId, "Ура! Записал тебя на каталку на выбранное время.");
    }
}
