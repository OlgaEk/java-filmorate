package ru.yandex.practicum.filmorate.errorHadler;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exception.*;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoSuchFilmIdException (final NoSuchFilmIdException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus (HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoSuchUserIdException (final NoSuchUserIdException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus (HttpStatus.CONFLICT)
    public ErrorResponse handleFriendAlreadyAddedException (final FriendAlreadyAddedException e){
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus (HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoSuchFriendException (final NoSuchFriendException e){
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus (HttpStatus.CONFLICT)
    public ErrorResponse handleLikeAlreadyAddedException (final LikeAlreadyAddedException e){
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus (HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoSuchLikeException (final NoSuchLikeException e){
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus (HttpStatus.BAD_REQUEST)
    public ErrorResponse handleCountException (final CountException e){
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        return new ErrorResponse(e.getFieldError().getDefaultMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoSuchGenreIdException (final NoSuchGenreIdException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoSuchMpaIdException (final NoSuchMpaIdException e) {
        return new ErrorResponse(e.getMessage());
    }


}
