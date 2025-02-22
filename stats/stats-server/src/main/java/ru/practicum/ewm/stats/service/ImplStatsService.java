package ru.practicum.ewm.stats.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.stats.EndpointHitDto;
import ru.practicum.ewm.dto.stats.ViewStats;
import ru.practicum.ewm.dto.stats.mappers.StatsMapper;
import ru.practicum.ewm.stats.exception.exceptions.BadRequestException;
import ru.practicum.ewm.stats.repository.StatsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImplStatsService implements StatsService {

    private final StatsRepository statsRepository;

    private static final DateTimeFormatter DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<ViewStats> getStats(String start, String end, List<String> uris, boolean unique) {

        if (start == null || end == null) {
            throw new BadRequestException("Параметры start и end обязательны");
        }

        LocalDateTime startTime = LocalDateTime.parse(start, DTF);
        LocalDateTime endTime = LocalDateTime.parse(end, DTF);

        if (startTime.isAfter(endTime)) {
            throw new BadRequestException("Дата начала не может быть позже даты конца");
        }



        return statsRepository.getStats(StatsMapper.toRequestMapper(start, end, uris, unique));

    }

    @Override
    public void recordHit(EndpointHitDto endpointHit) {
        statsRepository.recordHit(endpointHit);
    }
}
