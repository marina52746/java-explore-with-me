package ru.practicum.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewUserRequest {

    @NotBlank
    @Length(min = 2, max = 250)
    private String name;

    @Length(min = 6, max = 254)
    @Email
    @NotBlank
    private String email;
}
