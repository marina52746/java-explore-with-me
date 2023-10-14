package ru.practicum.event.enums;

import ru.practicum.exception.ValidationException;

public enum EventState {
    PENDING,
    PUBLISHED,
    CANCELED;

    public static EventState fromStringToEventState(String state) {
        switch (state) {
            case "PENDING":
                return PENDING;
            case "PUBLISHED":
                return PUBLISHED;
            case "CANCELED":
                return CANCELED;
        }
        throw new ValidationException("State not found", "State " + state + " not found");
    }
}
