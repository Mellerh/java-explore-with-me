package ru.practicum.ewm.service.user;

import org.springframework.data.domain.Pageable;
import ru.practicum.ewm.dto.user.NewUserRequest;
import ru.practicum.ewm.dto.user.UserDto;

import java.util.List;

public interface UserService {
    // admin
    // получение инфо о пользователях
    List<UserDto> getUserList(List<Long> idList, Pageable pageable);

    // добавление нового пользователя
    UserDto addUser(NewUserRequest newUserRequest);

    // удаление пользователя
    void deleteUser(Long userId);
}