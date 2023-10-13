package ru.practicum.event.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.category.model.Category;
import ru.practicum.category.mapper.CategoryMapper;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.enums.EventState;
import ru.practicum.event.model.Event;
import ru.practicum.location.Location;
import ru.practicum.location.LocationMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.mapper.UserMapper;

import java.time.LocalDateTime;

@Component
public class EventMapper {
    public static EventShortDto fromEventToEventShortDto(Event event) {
        if (event == null) return null;
        return new EventShortDto(
                event.getAnnotation(),
                CategoryMapper.fromCategoryToCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getEventDate(),
                event.getId(),
                UserMapper.fromUserToUserShortDto(event.getUser()),
                event.getPaid(),
                event.getTitle(),
                event.getViews()
        );
    }

    public static EventFullDto fromEventToEventFullDto(Event event) {
        if (event == null) return null;
        return new EventFullDto(
                event.getAnnotation(),
                CategoryMapper.fromCategoryToCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getCreatedOn(),
                event.getDescription(),
                event.getEventDate(),
                event.getId(),
                UserMapper.fromUserToUserShortDto(event.getUser()),
                LocationMapper.fromLocationToLocationDto(event.getLocation()),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn(),
                event.getRequestModeration(),
                event.getState(),
                event.getTitle(),
                event.getViews()
        );
    }

    public static Event fromNewEventDtoToEvent(User user, Category category,
                                               Location location, NewEventDto newEventDto) {
        if (newEventDto == null) return null;
        return new Event(
                0L,
                user,
                newEventDto.getAnnotation(),
                category,
                0,
                newEventDto.getEventDate(),
                newEventDto.getDescription(),
                newEventDto.getEventDate(),
                location,
                newEventDto.getPaid(),
                newEventDto.getParticipantLimit(),
                LocalDateTime.of(0, 1, 1, 0, 0, 0),
                newEventDto.getRequestModeration(),
                EventState.PENDING,
                newEventDto.getTitle(),
                0L
        );
    }
}
