package ru.practicum.event.enums;

import ru.practicum.exception.ValidationException;

public enum StateAction {
    SEND_TO_REVIEW, //user
    CANCEL_REVIEW, //user
    PUBLISH_EVENT, //admin
    REJECT_EVENT; //admin

    public static StateAction fromStringToStateActionUser(String state) {
        switch (state) {
            case "SEND_TO_REVIEW":
                return SEND_TO_REVIEW;
            case "CANCEL_REVIEW":
                return CANCEL_REVIEW;
        }
        throw new ValidationException("State action not found", "State action " + state + " for user not found");
    }

    public static StateAction fromStringToStateActionAdmin(String state) {
        switch (state) {
            case "PUBLISH_EVENT":
                return PUBLISH_EVENT;
            case "REJECT_EVENT":
                return REJECT_EVENT;
        }
        throw new ValidationException("State action not found", "State action " + state + " for admin not found");
    }
}


