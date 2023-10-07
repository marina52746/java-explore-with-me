package ru.practicum.controller;

import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.StatClient;
import ru.practicum.hit.HitDto;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Slf4j
@RestController
public class StatsController {
    private final StatClient statClient;

    @PostMapping(path = "/hit")
    public ResponseEntity<Object> create(@RequestBody HitDto hitDto) {
        log.info("Create hit: app=" + hitDto.getApp() + " ip=" + hitDto.getIp() + " uri=" + hitDto.getUri());
        return statClient.createHit(hitDto);
    }

    @GetMapping(path = "/stats")
    public ResponseEntity<Object> getStats(
            @RequestParam @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(required = false) String[] uris,
            @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Get stats: start=" + start + " end=" + end + " uris=" + uris + " unique=" + unique);
        return statClient.getStats(start, end, uris, unique);
    }
}
