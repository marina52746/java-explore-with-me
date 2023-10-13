package ru.practicum.event.filter;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import org.springframework.stereotype.Component;
import ru.practicum.event.model.QEvent;

@Component
public class UserFiltering implements UserFilterBuilder {
    private final QEvent EVENT = QEvent.event;

    public Predicate build(UserFilter filter) {
        BooleanBuilder builder = new BooleanBuilder(EVENT.isNotNull());
        if (!(filter.getText() == null)) {
            builder.and(EVENT.annotation.containsIgnoreCase(filter.getText())
                    .or(EVENT.description.containsIgnoreCase(filter.getText())));
        }
        if (!(filter.getCategories() == null)) {
            builder.and(EVENT.category.id.in(filter.getCategories()));
        }
        if (!(filter.getPaid() == null)) {
            builder.and(EVENT.paid.eq(filter.getPaid()));
        }
        if (filter.getRangeStart() != null) {
            builder.and(EVENT.eventDate.after(filter.getRangeStart()));
        }
        if (filter.getRangeEnd() != null) {
            builder.and(EVENT.eventDate.before(filter.getRangeEnd()));
        }
        if (filter.getOnlyAvailable() != null && filter.getOnlyAvailable()) {
            builder.and(EVENT.participantLimit.eq(0).or(EVENT.participantLimit.gt(EVENT.confirmedRequests)));
        }
        return builder;
    }
}