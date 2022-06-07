package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.NoSuchUserIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class UserService {

    HashMap<Long, User> userBase = new HashMap<>();
    private Long LastAssignedId = 0l;

    public List<User> getUsers(){
        ArrayList<User> users = new ArrayList<>();
        for(User user: userBase.values()){
            users.add(user);
        }
        return users;
    }

    public User addUser (User user){
        user.setId(++LastAssignedId);
        userBase.put(LastAssignedId,user);
        log.info("Пользователь {} добавлен в базу пользователей", user);
        return user;
    }

    public User updateUser (User user) throws NoSuchUserIdException {
        if(user.getId()==null || user.getId()==0){
            return addUser(user);
        }
        if(!userBase.containsKey(user.getId())){
            log.info("Id пользователя = {} не найдено в базе пользователей",user.getId());
            throw new NoSuchUserIdException("Пользатель не найден");
        }
            else {
            userBase.put(user.getId(),user);
            log.info("Данные пользователя {} обновлены",user);
            return user;
        }
    }
}
