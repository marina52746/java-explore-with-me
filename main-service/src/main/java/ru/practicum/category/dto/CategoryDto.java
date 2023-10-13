package ru.practicum.category.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.ReadOnlyProperty;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    @ReadOnlyProperty
    private Long id;

    @NotBlank
    @Length(min = 1, max = 50)
    private String name;
}
