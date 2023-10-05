package ru.practicum.user;

import org.springframework.stereotype.Controller;

@Controller
public class UserController {
    UserService userService;
/*
    @GetMapping
    List<User> findBy(String param1, String param2, String param3, String param4, String param5, String param6) {
        UserParam param = new UserParam(param1, param2, param3, param4, param5, param6);
        return UserService.findBy(param);
    }
 */
}
