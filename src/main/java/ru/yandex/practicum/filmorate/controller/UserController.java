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
    public List<User> allUsers(){ return userService.getAll();}

    @GetMapping("/{id}")
    public User getUser (@PathVariable Long id){ return userService.get(id);}

    @PostMapping
    public User createUser(@Valid @RequestBody User user){
        return userService.add( user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws NoSuchUserIdException {
        return userService.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public String addFriend (@PathVariable Long id,
                             @PathVariable Long friendId){
       return userService.addFriend(id,friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public String deleteFriend(@PathVariable Long id,
                               @PathVariable Long friendId){
        return userService.deleteFriend(id,friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable Long id){
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Long id,
                                       @PathVariable Long otherId){
        return userService.getCommonFriends(id,otherId);
        }

}
