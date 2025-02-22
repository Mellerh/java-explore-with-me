package ru.practicum.ewm.mapper;

import org.mapstruct.Mapper;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.model.Compilation;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CompilationMapper {

    default Compilation toCompilation(NewCompilationDto newCompilationDto) {
        Compilation entity = new Compilation();
        entity.setPinned(newCompilationDto.getPinned() != null && newCompilationDto.getPinned());
        entity.setTitle(newCompilationDto.getTitle());
        return entity;
    }

    CompilationDto toCompilationDto(Compilation compilation);

    List<CompilationDto> toCompilationDtoList(List<Compilation> compilationList);
}
