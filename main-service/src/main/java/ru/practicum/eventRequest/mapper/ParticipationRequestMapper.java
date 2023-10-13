package ru.practicum.eventRequest.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.eventRequest.dto.ParticipationRequestDto;
import ru.practicum.eventRequest.model.ParticipationRequest;

@Component
public class ParticipationRequestMapper {
    public static ParticipationRequestDto fromRequestToRequestDto(ParticipationRequest request) {
        if (request == null) return null;
        return new ParticipationRequestDto(
                request.getCreated(),
                request.getEvent(),
                request.getId(),
                request.getRequester(),
                request.getStatus()
        );
    }
}
