package ru.practicum.ewm.event.bd.model.enums;

import java.util.Optional;

public enum EventSort {

    EVENT_DATE,
    EVENT_COUNT,
    EVENT_ID;

    public static Optional<EventSort> from(String stringSort) {
        for (EventSort sort : values()) {
            if (sort.name().equalsIgnoreCase(stringSort)) {
                return Optional.of(sort);
            }
        }
        return Optional.empty();
    }

}
