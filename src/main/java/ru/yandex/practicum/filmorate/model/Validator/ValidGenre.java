package ru.yandex.practicum.filmorate.model.Validator;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidGenreValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidGenre {
    String message() default "Genre ID не найден";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

