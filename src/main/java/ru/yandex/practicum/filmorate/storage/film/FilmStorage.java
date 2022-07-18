package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FilmStorage {
    //Все методы интерфейса являются public abstract и  модификаторы необязательны.
    //методы добавления, удаления и модификации объектов
    Film add(Film film);
    Film get (Long id);
    Film delete(Film film);
    Film update(Film film);
    List<Film> getAll();
    boolean addLike(Long idFilm, Long IdUser);
    boolean deleteLike(Long idFilm, Long idUser);
    List<Film> getSortedByLikesFilm(Integer count);
    boolean containsFilmId (Long id);
}
