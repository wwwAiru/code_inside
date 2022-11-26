package ru.golikov.notes.domain.error.exception;

/**
 * исключение для тех случаев, когда пользователь регистрируется по ранее зарегестрированной почте
 * обрабатывается в GlobalExceptionHandler
 */
public class UserRegistrationException extends RuntimeException {
    public UserRegistrationException(String message) {
        super(message);
    }
}
