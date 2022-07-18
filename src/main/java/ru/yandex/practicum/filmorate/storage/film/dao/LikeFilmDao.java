package ru.yandex.practicum.filmorate.storage.film.dao;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface LikeFilmDao {
    boolean addLike(Long idFilm, Long IdUser);
    boolean deleteLike(Long idFilm, Long idUser);
    List<Long> getSortedByLikesFilm(Integer count);
}
