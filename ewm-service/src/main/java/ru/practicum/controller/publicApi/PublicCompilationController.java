package ru.practicum.controller.publicApi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.service.compilation.CompilationService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/compilations")
@Validated
public class PublicCompilationController {
    private final CompilationService service;

    @Autowired
    public PublicCompilationController(CompilationService service) {
        this.service = service;
    }

    @GetMapping()
    public ResponseEntity<List<CompilationDto>> allCompilations(
                        @RequestParam(required = false) boolean pinned,
                        @PositiveOrZero @RequestParam(defaultValue = "0") int from,
                        @Positive @RequestParam(defaultValue = "10") int size) {
        log.info("Получен GET-запрос к эндпоинту /compilations на получение списка подборок.");
        return new ResponseEntity<>(service.allCompilations(pinned, from, size), HttpStatus.OK);
    }

    @GetMapping("/{compId}")
    public ResponseEntity<CompilationDto> compilationById(@Positive @PathVariable long compId) {
        log.info("Получен GET-запрос к эндпоинту /compilations/{compId} на получение подборки по id.");
        return new ResponseEntity<>(service.compilationById(compId), HttpStatus.OK);
    }
}
