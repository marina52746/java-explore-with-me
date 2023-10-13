package ru.practicum.event.enums;

import ru.practicum.exception.NotFoundException;

public enum EventSort {
    EVENT_DATE,
    VIEWS;

    public static EventSort fromStringToEventSort(String sort) {
        switch (sort) {
            case "EVENT_DATE":
                return EVENT_DATE;
            case "VIEWS":
                return VIEWS;
            default:
                throw new NotFoundException("Sort not found", "Sort not found, sort=" + sort);
        }
    }
}
