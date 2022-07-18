package ru.yandex.practicum.filmorate.model.Validator;


import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ValidMpaValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidMpa {
    String message() default "MPA ID не найден";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

