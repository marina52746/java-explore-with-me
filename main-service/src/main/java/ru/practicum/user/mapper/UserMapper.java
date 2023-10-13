package ru.practicum.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.user.model.User;

@Component
public class UserMapper {
    public static UserDto fromUserToUserDto(User user) {
        if (user == null) return null;
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User fromUserDtoToUser(UserDto userDto) {
        if (userDto == null) return null;
        return new User(
                userDto.getId(),
                userDto.getName(),
                userDto.getEmail()
        );
    }

    public static User fromNewUserRequestToUser(NewUserRequest newUser) {
        if (newUser == null) return null;
        return new User(
                newUser.getName(),
                newUser.getEmail()
        );
    }

    public static UserShortDto fromUserToUserShortDto(User user) {
        if (user == null) return null;
        return new UserShortDto(
                user.getId(),
                user.getName()
        );
    }
}
