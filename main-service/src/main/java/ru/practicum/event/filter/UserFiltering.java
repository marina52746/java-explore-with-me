package ru.practicum.event.filter;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Component;
import ru.practicum.event.model.QEvent;

@Component
public class UserFiltering implements UserFilterBuilder {
    private final QEvent event = QEvent.event;

    public Predicate build(UserFilter filter) {
        BooleanBuilder builder = new BooleanBuilder(event.isNotNull());
        if (!(filter.getText() == null)) {
            builder.and(event.annotation.containsIgnoreCase(filter.getText())
                    .or(event.description.containsIgnoreCase(filter.getText())));
        }
        if (!(filter.getCategories() == null)) {
            builder.and(event.category.id.in(filter.getCategories()));
        }
        if (!(filter.getPaid() == null)) {
            builder.and(event.paid.eq(filter.getPaid()));
        }
        if (filter.getRangeStart() != null) {
            builder.and(event.eventDate.after(filter.getRangeStart()));
        }
        if (filter.getRangeEnd() != null) {
            builder.and(event.eventDate.before(filter.getRangeEnd()));
        }
        if (filter.getOnlyAvailable() != null && filter.getOnlyAvailable()) {
            builder.and(event.participantLimit.eq(0).or(event.participantLimit.gt(event.confirmedRequests)));
        }
        return builder;
    }
}