package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    //Все методы интерфейса являются public abstract и  модификаторы необязательны.
    //методы добавления, удаления и модификации объектов
    User add(User user);
    User delete(User user);
    User update(User user);
    List<User> getAll();
    User get (Long id);
    boolean containsUserId(Long id);
    boolean addFriend(Long id, Long idFriend);
    boolean deleteFriend(Long id, Long idFriend);
    List<User> getFriends(Long id);
    List<User> commonFriends(Long id, Long otherId);
}
