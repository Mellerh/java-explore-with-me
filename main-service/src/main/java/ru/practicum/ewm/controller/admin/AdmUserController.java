package ru.practicum.ewm.controller.admin;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.user.NewUserRequest;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.service.user.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
@Validated
public class AdmUserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getUserList(@RequestParam(required = false, name = "ids") List<Long> idList,
                                     @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                     @RequestParam(defaultValue = "10") @Positive Integer size) {
        return userService.getUserList(idList, PageRequest.of(from, size));
    }

    @PostMapping
    public UserDto addUser(@Valid @RequestBody NewUserRequest newUserRequest) {
        return userService.addUser(newUserRequest);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }

}
