package ru.practicum.ewm.service.user;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.user.NewUserRequest;
import ru.practicum.ewm.dto.user.UserDto;

import java.util.List;

@Service
public interface UserService {

    List<UserDto> getUserList(List<Long> idList, Pageable pageable);

    UserDto addUser(NewUserRequest newUserRequest);

    void deleteUser(Long userId);

}
