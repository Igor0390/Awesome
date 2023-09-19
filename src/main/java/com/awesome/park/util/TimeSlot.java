package com.awesome.park.util;

import java.time.LocalTime;

public enum TimeSlot {
    START_AT_WORK(LocalTime.of(10, 0)),
    END_AT_WORK(LocalTime.of(21, 0));
    private final LocalTime time;

    TimeSlot(LocalTime time) {
        this.time = time;
    }

    public LocalTime getTime() {
        return time;
    }
}
