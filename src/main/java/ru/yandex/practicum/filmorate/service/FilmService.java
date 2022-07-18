package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.LikeAlreadyAddedException;
import ru.yandex.practicum.filmorate.exception.NoSuchFilmIdException;
import ru.yandex.practicum.filmorate.exception.NoSuchLikeException;
import ru.yandex.practicum.filmorate.exception.NoSuchUserIdException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Slf4j
@Service
@Qualifier("filmDbStorage")
public class FilmService {

    private FilmStorage filmStorage;
    private UserStorage userStorage;

    @Autowired
    public FilmService (FilmStorage filmStorage, UserStorage userStorage){
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> getAll(){
        return filmStorage.getAll();
    }

    public Film get(Long id) {
        if( !filmStorage.containsFilmId (id) ) {
            log.info("Id фильма = {} не найдено в базе фильмов", id);
            throw new NoSuchFilmIdException("Фильм по ID = " + id + " не найден");
        }
        return filmStorage.get(id);
    }

    public Film add(Film film){
        return filmStorage.add(film);
    }

    public Film update(Film film) {
        if(film.getId() == null || film.getId() == 0){
            return filmStorage.add(film);
        }
        if( !filmStorage.containsFilmId (film.getId()) ){
            log.info("Id фильма = {} не найдено в базе фильмов",film.getId());
            throw new NoSuchFilmIdException("Фильм по ID = " + film.getId() + " не найден");
        }

        else {
            filmStorage.update(film);
            log.info("Фильм {} обновлен",film);
            return film;
        }
    }

    public boolean like(Long idFilm, Long idUser){
        if (!filmStorage.containsFilmId(idFilm)) throw new NoSuchFilmIdException("Фильм по ID = " + idFilm
                + " не найден");
        if(!userStorage.containsUserId(idUser)) throw new NoSuchUserIdException("Пользователь Id = "+ idUser
                + " не найден");
        if (!filmStorage.addLike(idFilm, idUser)) throw new LikeAlreadyAddedException("Пользователь ID = " + idUser
                + "уже ставил лайк фильму ID = " + idFilm);
        return true;
    }

    public boolean dislike(Long idFilm, Long idUser){
        if (!filmStorage.containsFilmId(idFilm)) throw new NoSuchFilmIdException("Фильм по ID = " + idFilm
                + " не найден");
        if(!userStorage.containsUserId(idUser)) throw new NoSuchUserIdException("Пользователь Id = "+ idUser
                + " не найден");
        if (!filmStorage.deleteLike (idFilm,idUser)) throw new NoSuchLikeException("Пользователь ID = " + idUser
                + "не ставил лайк фильму ID = " + idFilm);
        return true;

    }

    public List<Film> popular(Integer count){
        return filmStorage.getSortedByLikesFilm(count);
    }




}
