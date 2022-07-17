package ru.yandex.practicum.filmorate.storage.film.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreDao {
    List<Genre> getGenreByFilmID(Long id);
    void addGenres (Film film);
    List<Genre> getAllGenre();
    Genre getGenre(Integer id);
    boolean containsGenreId(Integer id);
    void removeGenres(Film film);
}
