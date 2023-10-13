package ru.practicum.stat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.exception.ValidationException;
import ru.practicum.hit.HitDto;
import ru.practicum.hit.HitMapper;
import ru.practicum.stat.InputStatDto;
import ru.practicum.stat.StatDto;
import ru.practicum.stat.repository.StatsRepository;
import ru.practicum.stat.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Autowired
    public StatsServiceImpl(StatsRepository statsRepository) {
        this.statsRepository = statsRepository;
    }

    @Override
    public HitDto create(HitDto hitDto) {
        hitDto.setTimestamp(LocalDateTime.now());
        return HitMapper.fromHitToHitDto(statsRepository.save(HitMapper.fromHitDtoToHit(hitDto)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<StatDto> getStats(InputStatDto inputStatDto) {
        if (inputStatDto.getStart().isAfter(inputStatDto.getEnd()))
            throw new ValidationException("Start must be before end", "Start must be before end");
         if (inputStatDto.getUnique()) {
            if (inputStatDto.getUris() == null)
                return statsRepository.findByStartEndUniqueIp(inputStatDto.getStart(), inputStatDto.getEnd());
            return statsRepository.findByStartEndUrisUniqueIp(inputStatDto.getStart(), inputStatDto.getEnd(),
                    inputStatDto.getUris());
        }
        if (inputStatDto.getUris() == null)
            return statsRepository.findByStartEndNotUniqueIp(inputStatDto.getStart(), inputStatDto.getEnd());
        return statsRepository.findByStartEndUrisNotUniqueIp(inputStatDto.getStart(), inputStatDto.getEnd(),
                inputStatDto.getUris());
    }
}
