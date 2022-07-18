package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.mbeans.UserMBean;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage{
    private HashMap<Long, User> userBase = new HashMap<>();
    private Long LastAssignedId = 0l;
    private HashMap<Long, Set<Long>> userFriendBase = new HashMap<>();

    public User add(User user){
        user.setId(++LastAssignedId);
        userBase.put(LastAssignedId,user);
        userFriendBase.put(user.getId(), new HashSet<Long>());
        log.info("Пользователь {} добавлен в базу пользователей", user);
        return user;
    }

    public List<User> getAll(){
        return userBase.values().stream().collect(Collectors.toList());
    }

    public User get (Long id){
        return userBase.get(id);
    }

    public User delete(User user){
        userBase.remove(user.getId());
        userFriendBase.remove(user.getId());
        userFriendBase.values().stream().filter(o->o.contains(user.getId())).forEach(o->o.remove(user.getId()));
        log.info("Пользователь {} удален из базы пользователей",user);
        return user;
    }

    public User update(User user){
        userBase.put(user.getId(), user);
        log.info("Данные пользователя {} обновлены",user);
        return user;
    }

    public boolean containsUserId(Long id){
        return userBase.containsKey(id);
    }

    public boolean addFriend(Long id, Long idFriend){
        return userFriendBase.get(id).add(idFriend);
    }

    public boolean deleteFriend(Long id, Long idFriend){
        return userFriendBase.get(id).remove(idFriend);
    }

    public List<User> getFriends(Long id){
        List<User> friends = new ArrayList<User>();
        userFriendBase.get(id).stream().forEach(o->friends.add(userBase.get(o)));
        return friends;
    }

    public List<User> commonFriends(Long id, Long otherId){
        ArrayList<Long> friendsOfId = new ArrayList<>(userFriendBase.get(id));
        return userFriendBase.get(otherId).stream()
                .filter(o->friendsOfId.contains(o))
                .map(userBase::get)
                .collect(Collectors.toList());
    }

    @Override
    public Integer statusOfFriendship(Long id, Long idFriend){
        return 0;
    }


}
