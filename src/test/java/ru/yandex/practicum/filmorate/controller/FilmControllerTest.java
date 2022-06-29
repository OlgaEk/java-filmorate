package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import ru.yandex.practicum.filmorate.exception.NoSuchFilmIdException;
import ru.yandex.practicum.filmorate.exception.NoSuchUserIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class FilmControllerTest {
    static FilmController filmController;
    Film film;
    private static Validator validator;
    Set<ConstraintViolation<Film>> violations;

    @BeforeAll
    static void initialization() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    @BeforeEach
    void configFilms() {
        filmController = new FilmController(new FilmService(new InMemoryFilmStorage(),new InMemoryUserStorage()));
        film = new Film();
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1990,1,1));
        film.setDuration(50);
    }
    @AfterEach
    void clean(){
        violations = null;
    }

    @Test
    void shouldCreateAndValidateFilm(){
        filmController.createFilms(film);
        assertEquals(1,filmController.getAllFilms().size());
        assertEquals("Name",filmController.getAllFilms().get(0).getName());
        violations = validator.validate(film);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldNotValidateFilmWithEmptyName(){
        film.setName("");
        violations = validator.validate(film);
        assertEquals(1,violations.size());
        assertEquals("не должно быть пустым",violations.iterator().next().getMessage());
    }

    @Test
    void shouldNotValidateFilmWithDescriptionMoreThan200symbols(){
        StringBuilder string200 = new StringBuilder();
        for(int i=0;i<=200;i++){
            string200.append("a");
        }
        film.setDescription(string200.toString());
        violations = validator.validate(film);
        assertEquals(1,violations.size());
        assertEquals("размер должен находиться в диапазоне от 0 до 200",
                violations.iterator().next().getMessage());
    }

    @Test
    void shouldNotValidateFilmIfReleaseDateIsWrong(){
        film.setReleaseDate(LocalDate.of(1895,12,27));
        violations = validator.validate(film);
        assertEquals(1,violations.size());
        assertEquals("дата релиза — не раньше 28 декабря 1895 года",violations.iterator().next().getMessage());
    }

    @Test
    void shouldNotValidateFilmIfDurationIsNegative(){
        film.setDuration(-1);
        violations = validator.validate(film);
        assertEquals(1,violations.size());
        assertEquals("должно быть не меньше 1",violations.iterator().next().getMessage());
    }

    @Test
    void shouldThrowExceptionIfFilmIdNotFound(){
        filmController.createFilms(film);
        film.setId(100l);
        assertThrows(NoSuchFilmIdException.class, () -> filmController.updateFilm(film));
    }

    @Test
    void shouldReturnBadRequestIfFilmBodyEmpty(@Autowired TestRestTemplate restTemplate) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ResponseEntity<String> response = restTemplate.exchange("/films",
                HttpMethod.POST,
                new HttpEntity<>(headers),
                String.class);
        assertEquals(HttpStatus.BAD_REQUEST,response.getStatusCode());
    }
}