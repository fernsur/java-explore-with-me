package ru.practicum.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Future;
import javax.validation.constraints.Size;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    @NotBlank(message = "Название события не может быть пустым")
    @Size(min = 3, max = 120)
    private String title;

    @NotBlank(message = "Аннотация события не может быть пустой")
    @Size(min = 20, max = 2000)
    private String annotation;

    @NotBlank(message = "Описание события не может быть пустым")
    @Size(min = 20, max = 7000)
    private String description;

    @Positive
    private Long category;

    @NotNull(message = "Локация события не может быть пустой")
    private LocationDto location;

    @Future
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("eventDate")
    private LocalDateTime eventDate;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

    private Long views;
}
