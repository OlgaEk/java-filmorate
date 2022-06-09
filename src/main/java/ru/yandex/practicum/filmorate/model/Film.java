package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;

@Data
public class Film {
    Long id; //целочисленный идентификатор — id;
    @NotBlank
    String name; //название — name;
    @Size(max=200)
    String description; //описание — description;
    @ValidDate
    private LocalDate releaseDate; //дата релиза — releaseDate;
    //@ValidDuration
    @Min(1)
    Integer duration; //продолжительность фильма — duration
}
