package ru.practicum.ewm.service.compilation;

import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.dto.compilation.UpdateCompilationRequest;

import java.util.List;

@Service
public interface CompilationService {

    List<CompilationDto> getCompilationList(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilation(Long compilationId);

    CompilationDto addCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(Long compilationId);

    CompilationDto updateCompilation(Long compilationId, UpdateCompilationRequest updateCompilationRequest);

}
