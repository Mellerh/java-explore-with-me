package ru.practicum.ewm.controller.pub;


import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.service.compilation.CompilationService;

import java.util.List;

@RestController
@RequestMapping(path = "/compilations")
@RequiredArgsConstructor
@Validated
public class PubCompilationController {

    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getCompilationList(@RequestParam(required = false) Boolean pinned,
                                                   @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
                                                   @RequestParam(required = false, defaultValue = "10") @Positive Integer size) {
        return compilationService.getCompilationList(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilation(@PathVariable Long compId) {
        return compilationService.getCompilation(compId);
    }

}
