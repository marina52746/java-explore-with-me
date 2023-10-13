package ru.practicum.controller;

import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.client.StatClient;
import ru.practicum.hit.HitDto;
import ru.practicum.stat.StatDto;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
public class StatsController {
    private final StatClient statClient;

    @PostMapping(path = "/hit")
    public ResponseEntity<Object> create(@RequestBody HitDto hitDto) {
        log.info("Create hit: app=" + hitDto.getApp() + " ip=" + hitDto.getIp() + " uri=" + hitDto.getUri());
        return new ResponseEntity<>(statClient.createHit(hitDto), HttpStatus.CREATED);
    }

    @GetMapping(path = "/stats")
    public ResponseEntity<List<StatDto>> getStats(
            @RequestParam @NotNull String start,
            @RequestParam String end,
            @RequestParam(required = false) String[] uris,
            @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Get stats: start=" + start + " end=" + end + " uris=" + uris + " unique=" + unique);
        return statClient.getStats(start, end, uris, unique);
    }
}
