package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FilmStorage {
    //методы добавления, удаления и модификации объектов
    public Film add(Film film);
    public Film get (Long id);
    public Film delete(Film film);
    public Film update(Film film);
    public List<Film> getAll();
    public boolean containsFilmId (Long id);
    public boolean addLike (Long idFilm, Long IdUser);
    public boolean deleteLike (Long idFilm, Long idUser);
    public Map<Long, Set<Long>> getAllLikes ();
    public List<Film> getSortedByLikesFilm(Integer count);

}
