package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Validator.ValidDate;
import ru.yandex.practicum.filmorate.model.Validator.ValidGenre;
import ru.yandex.practicum.filmorate.model.Validator.ValidMpa;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.List;


@Data
@Builder
public class Film {
    Long id; //целочисленный идентификатор — id;
    @NotBlank(message = "Название не должно быть пустым")
    String name; //название — name;
    @Size(max=200,message = "Описание фильма должно быть не больше 200 знаков" )
    String description; //описание — description;
    @ValidDate
    private LocalDate releaseDate; //дата релиза — releaseDate;
    //@ValidDuration
    @Min(value = 1,message = "Продолжительность фильма должна быть больше 1")
    Integer duration; //продолжительность фильма — duration
    @NotNull(message = "У фильма должен быть указан рейтинг MPA")
    @ValidMpa
    Mpa mpa;
    @ValidGenre
    List<Genre> genres;

}
