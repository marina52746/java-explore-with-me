package ru.practicum.eventRequest.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.eventRequest.enums.ParticipationStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequest {

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime created;

    @Column(name = "event", nullable = false)
    private Long event;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "requester", nullable = false)
    private Long requester;

    @Enumerated(EnumType.STRING)
    private ParticipationStatus status;
}
