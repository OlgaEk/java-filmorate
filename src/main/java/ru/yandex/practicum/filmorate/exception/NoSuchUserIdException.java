package ru.yandex.practicum.filmorate.exception;

public class NoSuchUserIdException extends Exception {
    public NoSuchUserIdException(String message){
        super(message);
    }
}
