package ru.yandex.practicum.filmorate.model;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidDateValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidDate {
        String message() default "дата релиза — не раньше 28 декабря 1895 года";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
}
