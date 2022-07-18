package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;


@Data
@Builder
public class User {
    private Long id; //целочисленный идентификатор — id;
    @NotBlank (message = "Email должен быть заполнен")
    @Email (message = "Email должен иметь формат адреса электронной почты")
    private String email; //электронная почта — email;
    @NotBlank(message = "Login должен быть заполнен")
    @Pattern(regexp = "^\\S*$" , message = "Логин не должен содержать пробелов")
    private String login; //логин пользователя — login;
    private String name; //имя для отображения — name;
    @Past (message = "Дата рождения указана неверно")
    private LocalDate birthday; //дата рождения — birthday.

    public void setName(String name) {
        this.name = name.isBlank() ? this.login : name;
    }
}
