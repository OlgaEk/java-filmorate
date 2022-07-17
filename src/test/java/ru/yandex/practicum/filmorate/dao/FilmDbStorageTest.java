package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.NoSuchFilmIdException;
import ru.yandex.practicum.filmorate.exception.NoSuchGenreIdException;
import ru.yandex.practicum.filmorate.exception.NoSuchMpaIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmDbStorageTest {
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;
    private final JdbcTemplate jdbcTemplate;
    private FilmController filmController;
    Film film;
    Film film2;
    User user;

    @BeforeEach
    void configFilms() {
        //filmController = new FilmController(new FilmService(new FilmDbStorage(new JdbcTemplate(), new GenreDaoImpl(new JdbcTemplate()),new MpaDaoImpl(new JdbcTemplate())),new UserDbStorage(new JdbcTemplate())));

        film = Film.builder()
                .name("Film_Name")
                .description("Film_description")
                .releaseDate(LocalDate.of(1990,02,05))
                .duration(100)
                .mpa(Mpa.builder().id(1).name("G").build())
                .genres(List.of(Genre.builder().id(1).name("Комедия").build()))
                .build();
        film2 = Film.builder()
                .name("Film2_Name")
                .description("Film2_description")
                .releaseDate(LocalDate.of(1995,02,05))
                .duration(110)
                .mpa(Mpa.builder().id(1).name("G").build())
                .genres(List.of(Genre.builder().id(1).name("Комедия").build()))
                .build();
        user = User.builder()
                .email("name@email.com")
                .login("login")
                .name("name")
                .birthday(LocalDate.of(1900,10,05))
                .build();
    }

    @AfterEach
    void clean() {
        String sql = "DELETE FROM films";
        jdbcTemplate.update(sql);
        sql = "ALTER TABLE films ALTER COLUMN film_id RESTART WITH 1";
        jdbcTemplate.update(sql);
    }


    @Test
    public void shouldAddUpdateAndDeleteFilm() {
        filmStorage.add(film);
        film.setId(1l);
        assertEquals(true,filmStorage.containsFilmId(1l));
        assertEquals(film,filmStorage.get(1l));
        film.setName("Film_updated");
        film.setGenres(List.of(Genre.builder().id(2).name("Драма").build()));
        filmStorage.update(film);
        assertEquals(film,filmStorage.get(1l));
        filmStorage.delete(film);
        assertThrows(NoSuchFilmIdException.class, () -> filmStorage.get(1l));
    }

    @Test
    public void shouldAddAndReturnTwoFilms(){
        filmStorage.add(film);
        filmStorage.add(film2);
        assertAll(
                ()->assertEquals(film,filmStorage.get(1l)),
                ()->assertEquals(film2,filmStorage.get(2l)),
                ()->assertEquals(2,filmStorage.getAll().size())
        );
    }

    @Test
    public void shouldAddLikeReturnSortedFilmAndDeleteLike(){
        filmStorage.add(film);
        filmStorage.add(film2);
        userStorage.add(user);
        filmStorage.addLike(1l,1l);
        assertEquals(false,filmStorage.addLike(1l,1l));
        assertEquals(1,filmStorage.getSortedByLikesFilm(1).get(0).getId());
        filmStorage.deleteLike(1l,1l);
        filmStorage.addLike(2l,1l);
        assertEquals(2,filmStorage.getSortedByLikesFilm(1).get(0).getId());
    }

    @Test
    public void shouldThrowExceptionIfMpaWrong(){
        film.setMpa(Mpa.builder().id(100).build());
        assertThrows(NoSuchMpaIdException.class, ()-> filmStorage.add(film));
    }
    @Test
    public void shouldThrowExceptionIfGenreWrong(){
        film.setGenres(List.of(Genre.builder().id(100).name("").build()));
        assertThrows(NoSuchGenreIdException.class, ()-> filmStorage.add(film));
    }

}

