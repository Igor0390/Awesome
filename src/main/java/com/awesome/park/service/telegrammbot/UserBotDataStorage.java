package com.awesome.park.service.telegrammbot;

import com.awesome.park.util.BotState;
import com.awesome.park.util.ConfirmationState;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserBotDataStorage {
    private final Map<Long, BotState> usersBotStates = new HashMap<>();
    private final Map<Long, String> usersPhoneNumbers = new HashMap<>();
    private final Map<Long, Boolean> isBookingTimeSelected = new HashMap<>();
    private final Map<Long, ConfirmationState> confirmationStates = new HashMap<>();

    public Map<Long, BotState> getUsersBotStates() {
        return usersBotStates;
    }

    public Map<Long, String> getUsersPhoneNumbers() {
        return usersPhoneNumbers;
    }

    public Map<Long, Boolean> getIsBookingTimeSelected() {
        return isBookingTimeSelected;
    }

    public Map<Long, ConfirmationState> getConfirmationStates() {
        return confirmationStates;
    }

}
