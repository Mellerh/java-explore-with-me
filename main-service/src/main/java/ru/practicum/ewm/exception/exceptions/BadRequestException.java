package ru.practicum.ewm.exception.exceptions;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String massage) {
        super(massage);
    }

}
