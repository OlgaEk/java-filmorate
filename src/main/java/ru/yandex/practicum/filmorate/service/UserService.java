package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchUserIdException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class UserService {
    private HashMap<Long, User> userBase = new HashMap<>();
    private Long LastAssignedId = 0l;

    public List<User> get(){
        ArrayList<User> users = new ArrayList<>();
        for(User user: userBase.values()){
            users.add(user);
        }
        return users;
    }

    public User add(User user){
        user.setId(++LastAssignedId);
        userBase.put(LastAssignedId,user);
        log.info("Пользователь {} добавлен в базу пользователей", user);
        return user;
    }

    public User update(User user) throws NoSuchUserIdException {
        if(user.getId()==null || user.getId()==0){
            return add(user);
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
