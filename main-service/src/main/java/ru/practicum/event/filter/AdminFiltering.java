package ru.practicum.event.filter;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Component;
import ru.practicum.event.enums.EventState;
import ru.practicum.event.model.QEvent;

import java.util.stream.Collectors;

@Component
public class AdminFiltering implements AdminFilterBuilder {
    private final QEvent EVENT = QEvent.event;

    public Predicate build(AdminFilter filter) {
        BooleanBuilder builder = new BooleanBuilder(EVENT.isNotNull());
        if (filter.getUsers() != null) {
            builder.and(EVENT.user.id.in(filter.getUsers()));
        }
        if (filter.getStates() != null) {
            builder.and(EVENT.state.in(filter.getStates().stream().map(EventState::fromStringToEventState)
                    .collect(Collectors.toList())));
        }
        if (filter.getCategories() != null) {
            builder.and(EVENT.category.id.in(filter.getCategories()));
        }
        if (filter.getRangeStart() != null) {
            builder.and(EVENT.eventDate.after(filter.getRangeStart()));
        }
        if (filter.getRangeEnd() != null) {
            builder.and(EVENT.eventDate.before(filter.getRangeEnd()));
        }
        return builder;
    }
}