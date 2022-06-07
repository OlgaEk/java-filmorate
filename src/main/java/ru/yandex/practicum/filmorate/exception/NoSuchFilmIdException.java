package ru.yandex.practicum.filmorate.exception;

public class NoSuchFilmIdException extends Exception {
    public NoSuchFilmIdException(String message){
        super(message);
    }
}
