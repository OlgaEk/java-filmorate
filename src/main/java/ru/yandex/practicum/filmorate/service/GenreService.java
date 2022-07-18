package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchFilmIdException;
import ru.yandex.practicum.filmorate.exception.NoSuchGenreIdException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.dao.GenreDao;

import java.util.List;

@Slf4j
@Service
public class GenreService {
    private GenreDao genreDao;

    @Autowired
    public GenreService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    public List<Genre> getAll(){
        return genreDao.getAllGenre();
    }

    public Genre get(Integer id) {
        if( !genreDao.containsGenreId(id) ) {
            log.info("Id жанра = {} не найдено ", id);
            throw new NoSuchGenreIdException("Жанр по ID = " + id + " не найден");
        }
        return genreDao.getGenre(id);
    }


}
