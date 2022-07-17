package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.storage.film.dao.MpaDao;
import ru.yandex.practicum.filmorate.storage.film.dao.MpaDaoImpl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDaoImplTest {
    private final MpaDaoImpl mpaDaoImpl;

    @Test
    public void shouldReturnAllMpa(){
       assertAll(
               ()->assertEquals(1,mpaDaoImpl.getAllMpa().get(0).getId()),
               ()->assertEquals("PG",mpaDaoImpl.getAllMpa().get(1).getName()),
               ()->assertEquals("NC-17",mpaDaoImpl.getAllMpa().get(4).getName())
       );
    }

    @Test
    public void shouldReturnFalseIfNotContainsMpaId(){
        assertAll(
                ()->assertEquals(true,mpaDaoImpl.containsMpaId(1)),
                ()->assertEquals(false,mpaDaoImpl.containsMpaId(100))
        );
    }

}
