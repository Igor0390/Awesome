package com.awesome.park.service.telegrambot;

import com.awesome.park.util.BotState;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Getter
public class UserBotDataStorage {
    private final Map<Long, BotState> usersBotStates = new HashMap<>();
}
