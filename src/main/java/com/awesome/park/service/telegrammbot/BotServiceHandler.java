package com.awesome.park.service.telegrammbot;

import com.awesome.park.util.BotState;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface BotServiceHandler {
    SendMessage handleStartCommand(String chatId, Message message);
    SendMessage handlePhoneNumberInput(Message message, String chatId, String phoneNumber, String name);
    SendMessage handleBookingTimeInput(Message message, String chatId, String phoneNumber, String name);
    BotState botState(Message message);
    boolean isValid(String phoneNumber);
}
