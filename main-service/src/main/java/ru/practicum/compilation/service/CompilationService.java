package ru.practicum.compilation.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationDto;

public interface CompilationService {
    ResponseEntity<Object> createCompilation(NewCompilationDto newCompilation);

    ResponseEntity<Object> deleteCompilation(Long compId);

    ResponseEntity<Object> updateCompilation(Long compId, UpdateCompilationDto updateCompilation);

    ResponseEntity<Object> getCompilations(Boolean pinned, Pageable pageable);

    ResponseEntity<Object> getCompilationById(Long compId);
}
