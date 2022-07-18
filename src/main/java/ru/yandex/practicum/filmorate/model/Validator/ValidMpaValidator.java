package ru.yandex.practicum.filmorate.model.Validator;


import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exception.NoSuchMpaIdException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.Validator.ValidMpa;
import ru.yandex.practicum.filmorate.storage.film.dao.MpaDao;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ValidMpaValidator implements ConstraintValidator<ValidMpa, Mpa> {
    @Autowired
     private MpaDao mpaDao;


    @Override
    public  boolean isValid(Mpa mpa, ConstraintValidatorContext cxt){
        if(mpa == null) return true;
        if(mpaDao.containsMpaId(mpa.getId())) return true;
        else {
            throw new NoSuchMpaIdException("MPA по ID = " + mpa.getId() + " не найден");
        }
    }
}
