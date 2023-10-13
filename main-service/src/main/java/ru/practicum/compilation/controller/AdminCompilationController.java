package ru.practicum.compilation.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.service.CompilationService;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationDto;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations")
public class AdminCompilationController {
    private final CompilationService compilationService;

    @PostMapping
    public ResponseEntity<Object> createCompilation(@Valid @RequestBody NewCompilationDto newCompilation) {
        return compilationService.createCompilation(newCompilation);
    }

    @DeleteMapping(path = "/{compId}")
    public ResponseEntity<Object> deleteCompilation(@PathVariable Long compId) {
        return compilationService.deleteCompilation(compId);
    }

    @PatchMapping(path = "/{compId}")
    public ResponseEntity<Object> updateCompilation(@PathVariable Long compId,
                                                    @Valid @RequestBody UpdateCompilationDto newCompilation) {
        return compilationService.updateCompilation(compId, newCompilation);
    }

}
