package ru.golikov.notes.domain.error.exception;

/**
 * исключение для тех случаев когда ресурс не найден
 * обрабатывается в GlobalExceptionHandler
 */
public class NotFoundException extends RuntimeException{

    public NotFoundException(String message) {
        super(message);
    }
}
