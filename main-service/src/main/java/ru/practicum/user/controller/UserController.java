package ru.practicum.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.pagination.FromSizeRequest;
import ru.practicum.user.service.UserService;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.ASC;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(required = false) Long[] ids,
                                  @RequestParam(required = false, defaultValue = "0") Integer from,
                                  @RequestParam(required = false, defaultValue = "10") Integer size) {
        final PageRequest pageRequest = FromSizeRequest.of(from, size, Sort.by(ASC, "id"));
        return userService.getUsers(ids, pageRequest);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@Valid @RequestBody NewUserRequest newUser) {
        return userService.createUser(newUser);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> delete(@PathVariable Long userId) {
        return userService.deleteUser(userId);
    }
}
