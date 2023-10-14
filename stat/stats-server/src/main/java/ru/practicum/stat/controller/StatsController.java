package ru.practicum.stat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.hit.HitDto;
import ru.practicum.stat.InputStatDto;
import ru.practicum.stat.StatDto;
import ru.practicum.stat.service.StatsService;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @PostMapping(path = "/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> create(@RequestBody HitDto hitDto) {
        return new ResponseEntity<>(statsService.create(hitDto), HttpStatus.CREATED);
    }

    @GetMapping(path = "/stats")
    public List<StatDto> getStats( //String time
            @RequestParam @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(required = false) String[] uris,
            @RequestParam(defaultValue = "false") Boolean unique) {
        return statsService.getStats(new InputStatDto(start, end, uris, unique));
    }
}
