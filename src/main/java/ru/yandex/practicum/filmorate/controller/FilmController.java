package ru.yandex.practicum.filmorate.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.CountException;
import ru.yandex.practicum.filmorate.exception.NoSuchFilmIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService){
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getAllFilms(){
        return filmService.getAll();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Long id) {
        return filmService.get(id);
    }

    @PostMapping
    public Film createFilms(@Valid @RequestBody Film film){
        return filmService.add(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) { return  filmService.update(film); }

    @PutMapping("/{id}/like/{userId}")
    public String addLikeToFilm(@PathVariable Long id,
                                @PathVariable Long userId){
        return filmService.like(id,userId);
    }

    @DeleteMapping ("/{id}/like/{userId}")
    public String deleteLike(@PathVariable Long id,
                             @PathVariable Long userId){
        return filmService.dislike(id, userId);
    }

    @GetMapping ("/popular")
    public List<Film> getPopularFilm(@RequestParam(defaultValue = "10") Integer count){
        if(count<=0) throw new CountException("Праметр count должен быть больше нуля");
        return filmService.popular(count);
    }

}
