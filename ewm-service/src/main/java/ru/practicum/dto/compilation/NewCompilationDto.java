package ru.practicum.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {
    @NotBlank(message = "Название подборки не может быть пустым")
    @Size(max = 50)
    private String title;

    private Boolean pinned;

    private List<Long> events;
}
