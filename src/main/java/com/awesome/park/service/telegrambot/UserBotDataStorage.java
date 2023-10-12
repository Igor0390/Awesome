package com.awesome.park.service.telegrambot;

import com.awesome.park.util.BotState;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserBotDataStorage {
    private final Map<Long, BotState> usersBotStates = new HashMap<>();


    public Map<Long, BotState> getUsersBotStates() {
        return usersBotStates;
    }







}
