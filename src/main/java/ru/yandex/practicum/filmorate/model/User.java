package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;


@Data
public class User {
    private Long id; //целочисленный идентификатор — id;
    @NotBlank
    @Email
    private String email; //электронная почта — email;
    @NotBlank
    @Pattern(regexp = "^\\S*$")
    private String login; //логин пользователя — login;
    private String name; //имя для отображения — name;
    @Past
    private LocalDate birthday; //дата рождения — birthday.

    public void setName(String name) {
        this.name = name.isBlank() ? this.login : name;
    }
}
