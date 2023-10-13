package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.event.enums.EventState;
import ru.practicum.location.LocationDto;
import ru.practicum.user.dto.UserShortDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventFullDto {

    @NotBlank
    private String annotation;

    @NotNull
    private CategoryDto category;

    private Integer confirmedRequests;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    private String description;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Long id;

    @NotNull
    private UserShortDto initiator;

    @NotNull
    private LocationDto location;

    @NotNull
    private Boolean paid;

    @Builder.Default
    private Integer participantLimit = 0;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;

    @Builder.Default
    private Boolean requestModeration = true;

    private EventState state;

    @NotBlank
    private String title;

    private Long views;

}
