package ru.yandex.practicum.filmorate.storage.film.dao;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

public interface MpaDao {
    List<Mpa> getAllMpa();
    Mpa getMpa(Integer id);
    boolean containsMpaId(Integer id);
}
