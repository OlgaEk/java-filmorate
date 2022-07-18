package ru.yandex.practicum.filmorate.model.Validator;

import ru.yandex.practicum.filmorate.model.Validator.ValidDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;


public class ValidDateValidator implements ConstraintValidator<ValidDate, LocalDate> {

    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext cxt) {
        return date.isAfter(LocalDate.of(1895,12,28));
    }
}