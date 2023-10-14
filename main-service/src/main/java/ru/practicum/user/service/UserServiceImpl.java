package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.exception.IntegrityViolationException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getUsers(Long[] ids, Pageable pageable) {
        Page<User> puser;
        if (ids == null) {
            puser = userRepository.findAll(pageable);
        } else {
            puser = userRepository.findByIdIn(ids, pageable);
        }
        return puser.getContent().stream()
                .map(UserMapper::fromUserToUserDto).collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<Object> createUser(NewUserRequest newUser) {
        if (userRepository.findByEmail(newUser.getEmail()) != null)
            throw new IntegrityViolationException("Integrity constraint has been violated", "Email not unique");
        return new ResponseEntity<>(UserMapper.fromUserToUserDto(
                userRepository.save(UserMapper.fromNewUserRequestToUser(newUser))), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Object> deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else
            throw new NotFoundException("The required object was not found",
                    "User with id=" + id + " was not found");
    }
}
