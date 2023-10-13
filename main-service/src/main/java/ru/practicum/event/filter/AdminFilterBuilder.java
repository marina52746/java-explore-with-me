package ru.practicum.event.filter;

import com.querydsl.core.types.Predicate;
import ru.practicum.event.filter.AdminFilter;

public interface AdminFilterBuilder {
    Predicate build(AdminFilter filter);
}