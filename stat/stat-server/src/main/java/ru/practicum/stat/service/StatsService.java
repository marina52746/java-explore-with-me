package ru.practicum.stat.service;
import ru.practicum.hit.HitDto;
import ru.practicum.stat.InputStatDto;
import ru.practicum.stat.StatDto;

import java.util.List;

public interface StatsService {

    HitDto create(HitDto hitDto);

    List<StatDto> getStats(InputStatDto inputStatDto);
}
