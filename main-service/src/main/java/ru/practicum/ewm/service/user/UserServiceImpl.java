package ru.practicum.ewm.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.ewm.dto.user.NewUserRequest;
import ru.practicum.ewm.dto.user.UserDto;
import ru.practicum.ewm.exception.exceptions.BadRequestException;
import ru.practicum.ewm.exception.exceptions.NotFoundException;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model.mapper.UserMapper;
import ru.practicum.ewm.repostirory.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    // !!ADMIN
    @Override
    public List<UserDto> getUserList(List<Long> idList, Pageable pageable) {
        if (idList == null) {
            return userMapper.toUserDtoList(userRepository.findAll(pageable).toList());
        } else {
            List<User> userList = new ArrayList<>();
            if (userRepository.existsByIdIn(idList)) {
                userList = userRepository.findAllByIdIn(idList, pageable).toList();
            }
            return userMapper.toUserDtoList(userList);
        }
    }

    @Override
    public UserDto addUser(NewUserRequest userDto) {
        if (userRepository.existsByName(userDto.getName())) {
            throw new BadRequestException("Пользователь уже существует: " + userDto.getName());
        }
        User userToSave = userMapper.toUser(userDto);
        userRepository.save(userToSave);
        return userMapper.toUserDto(userToSave);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).orElseThrow(() ->
                new NotFoundException("Пользователь с id " + userId + " уже существует"));
        userRepository.deleteById(userId);
    }

}
