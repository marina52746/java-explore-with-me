package ru.practicum.stat;
import ru.practicum.hit.HitDto;

import java.util.List;

public interface StatsService {
    HitDto create(HitDto hitDto);
    List<StatDto> getStats(InputStatDto inputStatDto);
}
