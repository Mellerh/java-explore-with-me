package ru.practicum.ewm.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.event.EventFullDto;
import ru.practicum.ewm.dto.event.UpdateEventAdminRequest;
import ru.practicum.ewm.service.event.EventService;

import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
@Validated
public class AdmEventController {

    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getEventListByAdmin(@RequestParam(name = "users", required = false) List<Long> userIdList,
                                                  @RequestParam(required = false) List<String> states,
                                                  @RequestParam(required = false) List<Long> categories,
                                                  @RequestParam(required = false) String rangeStart,
                                                  @RequestParam(required = false) String rangeEnd,
                                                  @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                  @RequestParam(defaultValue = "10") @Positive Integer size) {
        return eventService.getEventsByAdmin(userIdList, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long eventId,
                                    @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        return eventService.updateEventByAdmin(eventId, updateEventAdminRequest);
    }

}
