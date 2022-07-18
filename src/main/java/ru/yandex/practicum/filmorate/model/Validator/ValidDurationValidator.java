package ru.yandex.practicum.filmorate.model.Validator;

import ru.yandex.practicum.filmorate.model.Validator.ValidDuration;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Duration;

public class ValidDurationValidator implements ConstraintValidator<ValidDuration, Duration> {
    @Override
    public boolean isValid(Duration duration, ConstraintValidatorContext cxt) {
        return duration.toMinutes()>=0;
    }
}