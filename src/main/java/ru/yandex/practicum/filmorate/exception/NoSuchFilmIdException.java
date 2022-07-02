package ru.yandex.practicum.filmorate.exception;

public class NoSuchFilmIdException extends RuntimeException {
    public NoSuchFilmIdException(String message){
        super(message);
    }
}
