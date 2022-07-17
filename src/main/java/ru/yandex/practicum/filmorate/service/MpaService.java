package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NoSuchGenreIdException;
import ru.yandex.practicum.filmorate.exception.NoSuchMpaIdException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.dao.MpaDao;

import java.util.List;

@Slf4j
@Service
public class MpaService {
    private MpaDao mpaDao;

    @Autowired
    public MpaService(MpaDao mpaDao){
        this.mpaDao = mpaDao;
    }

    public List<Mpa> getAll(){
        return mpaDao.getAllMpa();
    }

    public Mpa get(Integer id) {
        if( !mpaDao.containsMpaId(id) ) {
            log.info("Id MPA = {} не найдено ", id);
            throw new NoSuchMpaIdException("MPA по ID = " + id + " не найден");
        }
        return mpaDao.getMpa(id);
    }
}
