package com.awesome.park.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Getter
public enum ActivityType {
    WAKE_BOARD(1L, "Катание на вейк-борде", BigDecimal.valueOf(600)),
    SUP_BOARD(2L, "Аренда сап-борда", BigDecimal.valueOf(600)),
    OTHER(3L, "Другая активность", BigDecimal.valueOf(600));

    private final Long id;
    private final String description;
    private final BigDecimal price;
}
