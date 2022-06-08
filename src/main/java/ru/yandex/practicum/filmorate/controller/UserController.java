package ru.yandex.practicum.filmorate.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NoSuchUserIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public List<User> allUsers(){
        return userService.get();
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user){
        return userService.add( user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws NoSuchUserIdException {
        return userService.update(user);
    }
}
