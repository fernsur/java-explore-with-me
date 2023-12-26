package ru.practicum.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private Long id;

    @NotBlank(message = "Название не может быть пустым.")
    @Length(min = 1, max = 50)
    private String name;
}
