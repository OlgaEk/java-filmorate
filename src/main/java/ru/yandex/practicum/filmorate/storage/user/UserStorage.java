package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    //методы добавления, удаления и модификации объектов
    public User add(User user);
    public User delete(User user);
    public User update(User user);
    public List<User> getAll();
    public User get (Long id);
    public boolean containsUserId(Long id);
    public boolean addFriend(Long id, Long idFriend);
    public boolean deleteFriend(Long id, Long idFriend);
    public List<User> getFriends(Long id);
    public List<User> commonFriends(Long id, Long otherId);
}
