package ru.practicum.service.compilation;

import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {
    CompilationDto createCompilation(CompilationDto dto);

    void deleteCompilation(long compId);

    CompilationDto updateCompilation(UpdateCompilationRequest dto, long compId);

    List<CompilationDto> allCompilations(boolean pinned, int from, int size);

    CompilationDto compilationById(long compId);
}
