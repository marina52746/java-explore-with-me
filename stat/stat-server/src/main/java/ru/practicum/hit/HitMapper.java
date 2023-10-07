package ru.practicum.hit;

import org.springframework.stereotype.Component;

@Component
public class HitMapper {
    public static Hit fromHitDtoToHit(HitDto hitDto) {
        if (hitDto == null) return null;
        return new Hit(
                hitDto.getId(),
                hitDto.getApp(),
                hitDto.getUri(),
                hitDto.getIp(),
                hitDto.getTimestamp()
        );
    }

    public static HitDto fromHitToHitDto(Hit hit) {
        if (hit == null) return null;
        return new HitDto(
                hit.getId(),
                hit.getApp(),
                hit.getUri(),
                hit.getIp(),
                hit.getTimestamp()
        );
    }
}
