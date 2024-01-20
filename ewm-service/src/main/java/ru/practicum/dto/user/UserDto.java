package ru.practicum.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank(message = "Имя не может быть пустым.")
    @Size(min = 2, max = 250)
    private String name;

    @Email(message = "Получена некорректная почта.")
    @NotEmpty(message = "Почта не может быть пустой.")
    @Size(min = 6, max = 254)
    private String email;
}
