package ru.practicum.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;

    @NotBlank(message = "Имя не может быть пустым.")
    @Length(min = 2, max = 250)
    private String name;

    @Email(message = "Получена некорректная почта.")
    @NotEmpty(message = "Почта не может быть пустой.")
    @Length(min = 6, max = 254)
    private String email;
}
