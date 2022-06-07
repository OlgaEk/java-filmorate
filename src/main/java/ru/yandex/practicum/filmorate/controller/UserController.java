package ru.yandex.practicum.filmorate.controller;


import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NoSuchUserIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    UserService userService = new UserService();

    @GetMapping
    public List<User> allUsers(){
        return userService.getUsers();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user){
        return userService.addUser( user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws NoSuchUserIdException {
        return userService.updateUser(user);
    }
}
