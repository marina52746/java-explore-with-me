package ru.practicum.event.filter;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Component;
import ru.practicum.event.enums.EventState;
import ru.practicum.event.model.QEvent;

import java.util.stream.Collectors;

@Component
public class AdminFiltering implements AdminFilterBuilder {
    private final QEvent event = QEvent.event;

    public Predicate build(AdminFilter filter) {
        BooleanBuilder builder = new BooleanBuilder(event.isNotNull());
        if (filter.getUsers() != null) {
            builder.and(event.user.id.in(filter.getUsers()));
        }
        if (filter.getStates() != null) {
            builder.and(event.state.in(filter.getStates().stream().map(EventState::fromStringToEventState)
                    .collect(Collectors.toList())));
        }
        if (filter.getCategories() != null) {
            builder.and(event.category.id.in(filter.getCategories()));
        }
        if (filter.getRangeStart() != null) {
            builder.and(event.eventDate.after(filter.getRangeStart()));
        }
        if (filter.getRangeEnd() != null) {
            builder.and(event.eventDate.before(filter.getRangeEnd()));
        }
        return builder;
    }
}