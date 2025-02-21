package ru.practicum.ewm.stats.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.stats.EndpointHitDto;
import ru.practicum.ewm.dto.stats.ViewStats;
import ru.practicum.ewm.stats.service.StatsService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class StatsController {

    private final StatsService statsService;


    @GetMapping("/stats")
    public List<ViewStats> getStats(@RequestParam(required = false)  String start,
                                    @RequestParam(required = false)  String end,
                                    @RequestParam(required = false) List<String> uris,
                                    @RequestParam(required = false) boolean unique) {
        return statsService.getStats(start, end, uris, unique);

    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void recordHit(@Valid @RequestBody EndpointHitDto endpointHitDto) {
        System.out.println(endpointHitDto);
        statsService.recordHit(endpointHitDto);
    }

}
