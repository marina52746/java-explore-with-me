package ru.practicum.compilation.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.dto.EventShortDto;

import java.util.ArrayList;
import java.util.List;

@Component
public class CompilationMapper {
    public static CompilationDto fromCompilationToCompilationDto(Compilation compilation, List<EventShortDto> events) {
        if (compilation == null) return null;
        return new CompilationDto(
                compilation.getId(),
                events,
                compilation.getPinned(),
                compilation.getTitle()
        );
    }

    public static Compilation fromNewCompilationDtoToCompilation(NewCompilationDto newCompilationDto) {
        if (newCompilationDto == null) return null;
        return new Compilation(
                fromListLongToString(newCompilationDto.getEvents()),
                newCompilationDto.getPinned(),
                newCompilationDto.getTitle()
        );
    }


    public static List<Long> fromStringToListLong(String eventsStr) {
        List<Long> eventsIds = new ArrayList<>();
        if (eventsStr.isBlank()) return eventsIds;
        String[] parts = eventsStr.split(";");
        for (var part : parts) {
            eventsIds.add((long) Integer.parseInt(part));
        }
        return eventsIds;
    }

    public static String fromListLongToString(List<Long> eventsIds) {
        if (eventsIds == null) return "";
        StringBuilder sb = new StringBuilder();
        for (Long id : eventsIds) {
            sb.append(id).append(';');
        }
        String str = sb.toString();
        return str.isBlank() ? "" : str.substring(0, str.length() - 1); // ili -2?
    }
}
