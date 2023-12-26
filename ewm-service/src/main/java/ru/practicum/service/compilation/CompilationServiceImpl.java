package ru.practicum.service.compilation;

import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.UpdateCompilationRequest;
import ru.practicum.repository.CompilationRepository;

import java.util.List;

public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository repository;

    @Autowired
    public CompilationServiceImpl(CompilationRepository repository) {
        this.repository = repository;
    }

    @Override
    public CompilationDto createCompilation(CompilationDto dto) {

    }

    @Override
    public void deleteCompilation(long compId) {

    }

    @Override
    public CompilationDto updateCompilation(UpdateCompilationRequest dto, long compId) {

    }

    @Override
    public List<CompilationDto> allCompilations(boolean pinned, int from, int size) {

    }

    @Override
    public CompilationDto compilationById(long compId) {

    }
}
