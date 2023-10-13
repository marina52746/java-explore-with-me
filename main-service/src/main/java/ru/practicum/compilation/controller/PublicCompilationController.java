package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.service.CompilationService;
import ru.practicum.pagination.FromSizeRequest;

import static org.springframework.data.domain.Sort.Direction.ASC;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
public class PublicCompilationController {
    private final CompilationService compilationService;

    @GetMapping
    public ResponseEntity<Object> getCompilations(@RequestParam(required = false) Boolean pinned,
                                            @RequestParam(required = false, defaultValue = "0") Integer from,
                                            @RequestParam(required = false, defaultValue = "10") Integer size) {
        final PageRequest pageRequest = FromSizeRequest.of(from, size, Sort.by(ASC, "id"));
        return compilationService.getCompilations(pinned, pageRequest);
    }

    @GetMapping(path = "/{compId}")
    public ResponseEntity<Object> getCompilationById(@PathVariable Long compId) {
        return compilationService.getCompilationById(compId);
    }
}
