package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FriendAlreadyAddedException;
import ru.yandex.practicum.filmorate.exception.NoSuchFriendException;
import ru.yandex.practicum.filmorate.exception.NoSuchUserIdException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class UserService {
    UserStorage userStorage;

    @Autowired
    public UserService (UserStorage userStorage){
        this.userStorage = userStorage;
    }

    public List<User> getAll(){
        return userStorage.getAll();
    }
    public User get(Long id){
        if(!userStorage.containsUserId (id)){
            log.info("Id пользователя = {} не найдено в базе пользователей",id);
            throw new NoSuchUserIdException("Пользатель по ID = " + id + " не найден");
        }
        return userStorage.get(id);
    }

    public User add(User user){
        return userStorage.add(user);
    }

    public User update(User user) {
        if(user.getId()==null || user.getId()==0){
            return userStorage.add(user);
        }
        if(!userStorage.containsUserId (user.getId())){
            log.info("Id пользователя = {} не найдено в базе пользователей",user.getId());
            throw new NoSuchUserIdException("Пользатель по ID = " + user.getId() + " не найден");
        }
            else {
            return userStorage.update(user);
        }
    }

    public String addFriend (Long id, Long idFriend){
        if(!userStorage.containsUserId(id)) throw new NoSuchUserIdException("Пользователь Id = "+ id + " не найден");
        if(!userStorage.containsUserId(idFriend)) throw new NoSuchUserIdException("Пользователь Id = "
                + idFriend + " не найден");
        if( !userStorage.addFriend(id,idFriend) ) throw new FriendAlreadyAddedException("Пользователь ID = " + idFriend
                + " уже был в списке друзей у пользователя ID = " + id);
        if( !userStorage.addFriend(idFriend,id) ) throw new FriendAlreadyAddedException("Пользователь ID = " + id
                + " уже был в списке друзей у пользователя ID = " + idFriend);
        return "Friend add";
    }

    public String deleteFriend(Long id, Long idFriend){
        if(!userStorage.containsUserId(id)) throw new NoSuchUserIdException("Пользователь Id = "+ id + " не найден");
        if(!userStorage.containsUserId(idFriend)) throw new NoSuchUserIdException("Пользователь Id = "
                + idFriend + " не найден");
        if(!userStorage.deleteFriend(id,idFriend)) throw new NoSuchFriendException("Пользователя ID = " + idFriend
                + " не было в списке друзей у пользователя ID = " + id);
        if(!userStorage.deleteFriend(idFriend,id)) throw new NoSuchFriendException("Пользователя ID = " + id
                + " не было в списке друзей у пользователя ID = " + idFriend);
        return "Friend deleted";
    }

    public List<User> getFriends(Long id){
        if(!userStorage.containsUserId(id)) throw new NoSuchUserIdException("Пользователь Id = "+ id + " не найден");
        return userStorage.getFriends(id);
    }

    public List<User> getCommonFriends(Long id, Long otherId){
        if(!userStorage.containsUserId(id)) throw new NoSuchUserIdException("Пользователь Id = "+ id + " не найден");
        if(!userStorage.containsUserId(otherId))
            throw new NoSuchUserIdException("Пользователь Id = "+ otherId + " не найден");
        return userStorage.commonFriends(id,otherId);

    }


}
