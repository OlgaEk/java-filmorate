package ru.yandex.practicum.filmorate.model;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.Duration;
import java.time.LocalDate;

public class ValidDurationValidator implements ConstraintValidator<ValidDuration, Duration> {
    @Override
    public boolean isValid(Duration duration, ConstraintValidatorContext cxt) {
        return duration.toMinutes()>=0;
    }
}