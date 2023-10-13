package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.mapper.CompilationMapper;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.model.Event;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.exception.NotFoundException;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    @Override
    public ResponseEntity<Object> createCompilation(NewCompilationDto newCompilation) {
        List<EventShortDto> eventShortDtos = new ArrayList<>();
        if (newCompilation.getEvents() != null)
            eventShortDtos = eventRepository.findByIdIn(newCompilation.getEvents()).stream()
                .map(EventMapper::fromEventToEventShortDto)
                .collect(Collectors.toList());
        Compilation compilation = compilationRepository.save(CompilationMapper.fromNewCompilationDtoToCompilation(
                newCompilation));
        return new ResponseEntity<>(CompilationMapper.fromCompilationToCompilationDto(compilation, eventShortDtos),
                HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Object> deleteCompilation(Long compId) {
        if (compilationRepository.existsById(compId)) {
            compilationRepository.deleteById(compId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
            throw new NotFoundException("The required object was not found",
                    "Compilation with id=" + compId + " was not found");
    }

    @Override
    public ResponseEntity<Object> updateCompilation(Long compId, UpdateCompilationDto newCompilation) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException("Compilation not found", "Compilation not found, id=" + compId));
        if (newCompilation.getEvents() != null)
            compilation.setEvents(CompilationMapper.fromListLongToString(newCompilation.getEvents()));
        if (newCompilation.getPinned() != null)
            compilation.setPinned(newCompilation.getPinned());
        if (newCompilation.getTitle() != null)
            compilation.setTitle(newCompilation.getTitle());
        compilationRepository.save(compilation);

        List<Event> events = eventRepository.findByIdIn(
                CompilationMapper.fromStringToListLong(compilation.getEvents()));
        List<EventShortDto> eventShortDtos = events.stream()
                .map(EventMapper::fromEventToEventShortDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(CompilationMapper.fromCompilationToCompilationDto(compilation, eventShortDtos),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getCompilations(Boolean pinned, Pageable pageable) {
        List<Compilation> compilations;
        List<CompilationDto> compilationDtos = new ArrayList<>();
        Map<Long, List<Long>> eventIds = new HashMap<>(); //compilationId, eventIds
        List<Long> allEventsIds = new ArrayList<>();
        Map<Long, EventShortDto> eventDtos = new HashMap<>(); //id, eventshortdto
        if (pinned == null)
            compilations = compilationRepository.findAll(pageable).getContent();
        else
            compilations = compilationRepository.findAllByPinned(pinned, pageable).getContent();
        compilations.stream().forEach(x -> {
            List<Long> evIds = CompilationMapper.fromStringToListLong(x.getEvents());
            eventIds.put(x.getId(), evIds);
            allEventsIds.addAll(evIds);
        });
        eventRepository.findByIdIn(allEventsIds).stream()
                .map(EventMapper::fromEventToEventShortDto)
                .forEach(x -> eventDtos.put(x.getId(), x));
        return new ResponseEntity<>(compilations.stream().map(x ->
                CompilationMapper.fromCompilationToCompilationDto(x,
                        eventIds.get(x.getId()).stream()
                                .map(eventDtos::get).collect(Collectors.toList()))),
                HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Object> getCompilationById(Long compId) {
        if (!(compilationRepository.existsById(compId)))
            throw new NotFoundException("Compilation not found", "Compilation not found, id=" + compId);
        Compilation compilation = compilationRepository.findById(compId).get();

        List<Event> events = eventRepository.findByIdIn(
                CompilationMapper.fromStringToListLong(compilation.getEvents()));
        Long[] eventsIds = events.stream().map(Event::getId).toArray(Long[]::new);
        List<EventShortDto> eventShortDtos = events.stream()
                .map(EventMapper::fromEventToEventShortDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(CompilationMapper.fromCompilationToCompilationDto(compilation, eventShortDtos),
                HttpStatus.OK);
    }
}
