package com.awesome.park.service.telegrammbot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.time.Instant;

public interface BotServiceHandler {
    SendMessage handleStartCommand(String chatId);
    SendMessage handlePhoneNumberInput(Message message, String chatId, String phoneNumber, String name);
    SendMessage handleBookingTimeInput(Message message, String chatId, String phoneNumber, String name);
    SendMessage sendConfirmationRequest(String chatId);
    SendMessage handleConfirmationResponse(String chatId, Message message, String s);
    String getSecondTime(Message message);
}
