package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.NoSuchUserIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;
    private final JdbcTemplate jdbcTemplate;
    private FilmController filmController;
    Film film;
    Film film2;
    User user;
    User user2;
    User user3;

    @BeforeEach
    void configFilms() {
        film = Film.builder()
                .name("Film_Name")
                .description("Film_description")
                .releaseDate(LocalDate.of(1990, 02, 05))
                .duration(100)
                .mpa(Mpa.builder().id(1).name("G").build())
                .genres(List.of(Genre.builder().id(1).name("Комедия").build()))
                .build();
        user = User.builder()
                .email("name@email.com")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1900, 10, 05))
                .build();
        user2 = User.builder()
                .email("name2@email.com")
                .login("login2")
                .name("name2")
                .birthday(LocalDate.of(1900, 10, 02))
                .build();
        user3 = User.builder()
                .email("name3@email.com")
                .login("login3")
                .name("name3")
                .birthday(LocalDate.of(1900, 10, 03))
                .build();
    }

    @AfterEach
    void clean() {
        String sql = "DELETE FROM users";
        jdbcTemplate.update(sql);
        sql = "ALTER TABLE users ALTER COLUMN user_id RESTART WITH 1";
        jdbcTemplate.update(sql);
    }

    @Test
    public void shouldAddUpdateAndDeleteUser(){
        userStorage.add(user);
        user.setId(1l);
        assertEquals(true,userStorage.containsUserId(1l));
        assertEquals(user,userStorage.get(1l));
        user.setName("user_updated");
        userStorage.update(user);
        assertEquals(user,userStorage.get(1l));
        userStorage.delete(user);
        assertThrows(NoSuchUserIdException.class, () -> userStorage.get(1l));
    }

    @Test
    public void shouldAddAndReturnTwoUsers(){
        userStorage.add(user);
        userStorage.add(user2);
        assertAll(
                ()->assertEquals(user,userStorage.get(1l)),
                ()->assertEquals(user2,userStorage.get(2l)),
                ()->assertEquals(2,userStorage.getAll().size())
        );
    }

    @Test
    public void ShouldAddFriendGetFriendDeleteFriendAndReturnCommonFriends(){
        userStorage.add(user);
        userStorage.add(user2);
        userStorage.add(user3);
        userStorage.addFriend(1l,2l);
        assertEquals(false,userStorage.addFriend(1l,2l));
        assertEquals(2,userStorage.getFriends(1l).get(0).getId());
        assertEquals(0,userStorage.statusOfFriendship(1l,2l));
        userStorage.addFriend(2l,1l);
        assertEquals(1,userStorage.statusOfFriendship(1l,2l));
        assertEquals(1,userStorage.statusOfFriendship(2l,1l));
        userStorage.addFriend(1l,3l);
        userStorage.addFriend(2l,3l);
        assertEquals(3,userStorage.commonFriends(1l,2l).get(0).getId());
        userStorage.deleteFriend(2l,3l);
        userStorage.deleteFriend(2l,1l);
        assertEquals(0,userStorage.getFriends(2l).size());
    }

}