package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NoSuchFilmIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {

    private FilmService filmService = new FilmService();

    @GetMapping
    public List<Film> getAllFilms(){
        return filmService.getFilms();
    }

    @PostMapping
    public Film createFilms(@Valid @RequestBody Film film){
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws NoSuchFilmIdException {
       return  filmService.updateFilm(film);
    }






}
