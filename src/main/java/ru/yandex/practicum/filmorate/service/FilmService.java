package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exception.NoSuchFilmIdException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
public class FilmService {
    private HashMap<Long, Film> filmBase = new HashMap<>();
    private Long LastAssignedId = 0l;

    public List<Film> getFilms(){
        ArrayList<Film> films = new ArrayList<>();
        for(Film film : filmBase.values()){
            films.add(film);
        }
        return films;}

    public Film addFilm(Film film){
        film.setId(++LastAssignedId);
        filmBase.put(LastAssignedId,film);
        log.info("Фильм {} добавлен в базу",film);
        return film;
    }

    public Film updateFilm(Film film) throws NoSuchFilmIdException {
        if(film.getId()==null || film.getId() == 0){
            return addFilm(film);
        }
        if( !filmBase.containsKey(film.getId()) ){
            log.info("Id фильма = {} не найдено в базе фильмов",film.getId());
            throw new NoSuchFilmIdException("Фильм не найден по Id");
        }

        else {
            filmBase.put(film.getId(),film);
            log.info("Фильм {} обновлен",film);
            return film;
        }
    }
}
