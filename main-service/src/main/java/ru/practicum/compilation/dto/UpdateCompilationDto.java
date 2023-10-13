package ru.practicum.compilation.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCompilationDto {
    private List<Long> events;

    private Boolean pinned = false;

    @Length(min = 1, max = 50)
    private String title;
}
