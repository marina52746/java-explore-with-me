package ru.practicum.stat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.hit.HitDto;
import ru.practicum.hit.HitMapper;

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
