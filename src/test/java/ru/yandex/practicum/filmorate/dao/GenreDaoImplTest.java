package ru.yandex.practicum.filmorate.dao;


import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.storage.film.dao.GenreDaoImpl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDaoImplTest {
    private final GenreDaoImpl genreDao;

    @Test
    public void shouldReturnAllGenre(){
        assertAll(
                ()->assertEquals(1,genreDao.getAllGenre().get(0).getId()),
                ()->assertEquals("Драма",genreDao.getAllGenre().get(1).getName()),
                ()->assertEquals("Документальный",genreDao.getAllGenre().get(4).getName())
        );
    }

    @Test
    public void shouldReturnFalseIfNotContainsMpaId(){
        assertAll(
                ()->assertEquals(true,genreDao.containsGenreId(1)),
                ()->assertEquals(false,genreDao.containsGenreId(100))
        );
    }




}
