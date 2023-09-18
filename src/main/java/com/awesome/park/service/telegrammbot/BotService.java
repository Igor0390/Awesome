package com.awesome.park.service.telegrammbot;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface BotService {
    void executeSafely(SendMessage message);
}
