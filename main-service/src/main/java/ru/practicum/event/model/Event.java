package ru.practicum.event.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.category.model.Category;
import ru.practicum.event.enums.EventState;
import ru.practicum.location.Location;
import ru.practicum.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private User user;

    @Column(name = "annotation")
    private String annotation;

    @ManyToOne
    @JoinColumn(name = "categories_id", nullable = false)
    private Category category;

    @Column(name = "confirmed_requests")
    @Builder.Default
    private Integer confirmedRequests = 0;

    @Column(name = "created_on")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    @Column(name = "description")
    private String description;

    @Column(name = "eventdate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @ManyToOne
    @JoinColumn(name = "locations_id", nullable = false)
    private Location location;

    @Column(name = "paid")
    @Builder.Default
    private Boolean paid = false;

    @Column(name = "participantlimit")
    @Builder.Default
    private Integer participantLimit = 0;

    @Column(name = "published_on")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;

    @Column(name = "requestmoderation")
    @Builder.Default
    private Boolean requestModeration = true;

    @Enumerated(EnumType.STRING)
    private EventState state;

    @Column(name = "title")
    private String title;

    @Column(name = "views")
    @Builder.Default
    private Long views = 0L;

}
