package ru.practicum.user.service;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;

import java.util.List;

public interface UserService {

    List<UserDto> getUsers(Long[] ids, Pageable pageable);

    ResponseEntity<Object> createUser(NewUserRequest newUser);

    ResponseEntity<Object> deleteUser(Long id);

}
