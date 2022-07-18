package ru.yandex.practicum.filmorate.storage.user.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendshipDao {
    boolean addFriend(Long id, Long idFriend);
    boolean deleteFriend(Long id, Long idFriend);
    List<Long> getFriends(Long id);
    List<Long> commonFriends(Long id, Long otherId);
    Integer statusOfFriendship(Long id, Long idFriend);
}
