package ru.practicum.ewm.service.compilation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.client.stats.StatsClient;
import ru.practicum.ewm.dto.compilation.CompilationDto;
import ru.practicum.ewm.dto.compilation.NewCompilationDto;
import ru.practicum.ewm.dto.compilation.UpdateCompilationRequest;
import ru.practicum.ewm.dto.event.EventShortDto;
import ru.practicum.ewm.dto.stats.ViewStats;
import ru.practicum.ewm.dto.stats.ViewStatsRequest;
import ru.practicum.ewm.enums.RequestStatus;
import ru.practicum.ewm.errorHandler.exceptions.NotFoundException;
import ru.practicum.ewm.mapper.CompilationMapper;
import ru.practicum.ewm.model.Compilation;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.repository.CompilationRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.RequestRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compilationRepository;
    private final CompilationMapper compilationMapper;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final StatsClient statsClient;

    // public
    // получение подборок событий
    @Override
    public List<CompilationDto> getCompilationList(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);

        Page<Compilation> compilationPage;

        if (pinned != null) {
            compilationPage = compilationRepository.findAllByPinnedOrderByIdDesc(pinned, pageable);
        } else {
            compilationPage = compilationRepository.findAll(pageable);
        }

        List<Compilation> compilationList = compilationPage.getContent();

        List<CompilationDto> compilationDtoList = new ArrayList<>();
        // к каждому event в каждой compilation нужно добавить сonfirmedRequests & views
        for (Compilation compilation : compilationList) {
            CompilationDto compilationDto = compilationMapper.toCompilationDto(compilation);
            compilationDtoList.add(addConfirmedRequestsAndViews(compilationDto));
        }

        return compilationDtoList;
    }

    // получение подборки событие по его id
    @Override
    public CompilationDto getCompilation(Long compilationId) {
        Compilation compilation = compilationRepository.findById(compilationId)
                .orElseThrow(() -> new NotFoundException("Compilation не существует" + compilationId));

        // переводим в ДТО и сохраняем ConfirmedRequestsAndViews
        CompilationDto compilationDto = compilationMapper.toCompilationDto(compilation);
        return addConfirmedRequestsAndViews(compilationDto);
    }

    // admin
    // добавление новой подборки
    @Override
    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        // если в подборке уже есть какие-то события, то их нужно сохранить
        if (newCompilationDto.getEvents() != null && newCompilationDto.getEvents().size() != 0) {
            // получаем список id событий в этой подборке
            Set<Long> eventIdList = newCompilationDto.getEvents();

            // выгружаем все события
            Set<Event> events = eventRepository.findAllByIdIn(eventIdList);
            Compilation compilation = compilationMapper.toCompilation(newCompilationDto);

            // сохраняем все события в подборке и сохраняем в репозитории
            compilation.setEvents(events);
            compilationRepository.save(compilation);

            // переводим в ДТО и сохраняем ConfirmedRequestsAndViews
            CompilationDto compilationDto = compilationMapper.toCompilationDto(compilation);
            return addConfirmedRequestsAndViews(compilationDto);
        }

        // новая подборка без событий
        Compilation compilation = compilationMapper.toCompilation(newCompilationDto);
        if (compilation.getEvents() == null) {
            compilation.setEvents(new HashSet<>());
        }
        compilationRepository.save(compilation);
        return compilationMapper.toCompilationDto(compilation);
    }


    // удаление подборки
    @Override
    public void deleteCompilation(Long compilationId) {
        compilationRepository.findById(compilationId).orElseThrow(() ->
                new NotFoundException("Compilation не существует" + compilationId));
        compilationRepository.deleteById(compilationId);
    }

    // обновить информацию о подборке
    @Override
    public CompilationDto updateCompilation(Long compilationId, UpdateCompilationRequest updateCompilationRequest) {
        // выгружаем подборку из БД
        Compilation compilation = compilationRepository.findById(compilationId).orElseThrow(() ->
                new NotFoundException("Compilation не существует" + compilationId));

        // если в присланной подборке есть события, то сохраняем их в выгруженной подборке
        if (updateCompilationRequest.getEvents() != null && !updateCompilationRequest.getEvents().isEmpty()) {
            Set<Long> eventIdList = updateCompilationRequest.getEvents();
            Set<Event> events = eventRepository.findAllByIdIn(eventIdList);
            compilation.setEvents(events);
        }
        // обновляем закреплено на главной странице или нет
        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }
        // обновляем название/заголовок
        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        // сохраняем в БД
        compilationRepository.save(compilation);

        CompilationDto compilationDto = compilationMapper.toCompilationDto(compilation);

        return addConfirmedRequestsAndViews(compilationDto);
    }

    private CompilationDto addConfirmedRequestsAndViews(CompilationDto compilationDto) {
        for (EventShortDto eventDto : compilationDto.getEvents()) {
            // Добавить сonfirmedRequests к каждому событию
            eventDto.setConfirmedRequests(
                    requestRepository.countByEventIdAndStatus(eventDto.getId(), RequestStatus.CONFIRMED));

            // Добавить views к каждому событию
            List<String> uris = new ArrayList<>();

            // создаем uri для обращения к базе данных статистики
            uris.add("/events/" + eventDto.getId());
            ViewStatsRequest viewStatsRequest = new ViewStatsRequest(
                    LocalDateTime.now().minusYears(100),
                    LocalDateTime.now(),
                    uris,
                    true);
            List<ViewStats> viewStatsList = statsClient.getStats(viewStatsRequest);
            if (viewStatsList.isEmpty()) {
                eventDto.setViews(0L);
            } else {
                eventDto.setViews(viewStatsList.get(0).getHits());
            }
        }
        return compilationDto;
    }
}

