package ru.yandex.practicum.filmorate.model.Validator;


import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.exception.NoSuchGenreIdException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.dao.GenreDao;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

// Приложение работает корректно и валидирует Film, в том числе и на наличие корректных MPA  и Genre.
// Я убрала внутренние тесты на Валидацию Film.
// Все тесты для валидации проходили только при добавлении Validator и проверки объекта через него.
// Но теперь в кастомной валидации на уровне объекта Film необходимо внедрять бин GenreDao.
// Но это внедрение не нравится Validator он никак не пропускает.
// Не помогло и установка дефолтного конструктора в классе ValidGenreValidator и ValidMpaValidator.
// Может вы подскажете, в каком напрвлении двигаться , чтобы решить проблему с Validator или
// как напрямую тестировать аннотации?
// Если тесты сейчас необходимы, то валидацию нужно переместить на уровень service,
// но мне кажется валидация через аннотации более читабельна.


public class ValidGenreValidator implements ConstraintValidator<ValidGenre, List<Genre>> {
    final GenreDao genreDao;

    @Autowired
    public ValidGenreValidator(GenreDao genreDao) {
        this.genreDao = genreDao;
    }


    @Override
    public  boolean isValid(List<Genre> genres, ConstraintValidatorContext cxt){
        if(genres == null || genres.size() == 0) return true;
        genres.stream()
                .forEach((k)-> {
                    if(!genreDao.containsGenreId(k.getId())){
                        throw new NoSuchGenreIdException("Жанр по ID = " + k.getId() + " не найден");
                        }
                });
        return true;
    }
}

