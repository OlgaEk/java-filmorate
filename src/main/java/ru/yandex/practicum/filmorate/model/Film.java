package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;
import java.sql.Date;
import java.util.HashMap;
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
    Mpa mpa;
    List<Genre> genres;

}
