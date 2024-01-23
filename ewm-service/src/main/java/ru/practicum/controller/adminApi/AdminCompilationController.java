package ru.practicum.controller.adminApi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.NewCompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;
import ru.practicum.service.compilation.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Slf4j
@RestController
@RequestMapping(path = "/admin/compilations")
@Validated
public class AdminCompilationController {
    private final CompilationService service;

    @Autowired
    public AdminCompilationController(CompilationService service) {
        this.service = service;
    }

    @PostMapping()
    public ResponseEntity<CompilationDto> createCompilation(@RequestBody @Valid NewCompilationDto dto) {
        log.info("Получен POST-запрос к эндпоинту /admin/compilations на добавление подборки.");
        return new ResponseEntity<>(service.createCompilation(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Void> deleteCompilation(@Positive @PathVariable long compId) {
        log.info("Получен DELETE-запрос к эндпоинту /admin/compilations/{compId} на удаление подборки.");
        service.deleteCompilation(compId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> updateCompilation(@RequestBody @Valid UpdateCompilationRequest dto,
                                                            @Positive @PathVariable long compId) {
        log.info("Получен PATCH-запрос к эндпоинту /admin/compilations/{compId} на обновление подборки.");
        return new ResponseEntity<>(service.updateCompilation(dto, compId), HttpStatus.OK);
    }
}
