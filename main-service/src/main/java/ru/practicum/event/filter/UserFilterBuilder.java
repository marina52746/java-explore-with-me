package ru.practicum.event.filter;

import com.querydsl.core.types.Predicate;
import ru.practicum.event.filter.UserFilter;

public interface UserFilterBuilder {
    Predicate build(UserFilter filter);
}