package ru.practicum.stat;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InputStatDto {
    private Long id;

    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime start;

    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime end;

    private String[] uris;

    private Boolean unique;

    public InputStatDto(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique) {
        this.start = start;
        this.end = end;
        this.uris = uris;
        this.unique = unique;
    }
}
