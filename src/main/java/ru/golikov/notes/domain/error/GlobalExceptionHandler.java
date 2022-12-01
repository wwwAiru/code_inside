package ru.golikov.notes.domain.error;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.golikov.notes.domain.error.dto.ErrorDetailsDto;
import ru.golikov.notes.domain.error.exception.NotFoundException;
import ru.golikov.notes.domain.error.exception.UserRegistrationException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * Метод отлавливает ошибки валидации
	 * @param ex - исключение вызванное валидацией
	 * @param headers - заголовки
	 * @param status - код
	 * @param request - веб реквест
	 * @return
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
																  HttpHeaders headers,
																  HttpStatus status,
																  WebRequest request) {
		Map<String, Object> body = new LinkedHashMap<>();
		Map<String, Set<String>> errors = ex.getBindingResult()
				.getFieldErrors()
				.stream()
				.collect(Collectors.groupingBy(FieldError::getField, Collectors.mapping(FieldError::getDefaultMessage, toSet())));
		body.put("timestamp", LocalDateTime.now());
		body.put("errors", errors);
		return new ResponseEntity<>(body, headers, status);
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<?> notFoundException(NotFoundException ex, WebRequest request) {
		ErrorDetailsDto errorDetailsDto = ErrorDetailsDto.builder()
				.timestamp(LocalDateTime.now())
				.message(ex.getMessage())
				.details(request.getDescription(false))
				.build();
		return new ResponseEntity<>(errorDetailsDto, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserRegistrationException.class)
	public ResponseEntity<?> userRegistrationException(UserRegistrationException ex, WebRequest request) {
		ErrorDetailsDto errorDetailsDto = ErrorDetailsDto.builder()
				.timestamp(LocalDateTime.now())
				.message(ex.getMessage())
				.details(request.getDescription(false))
				.build();
		return new ResponseEntity<>(errorDetailsDto, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest request) {
		ErrorDetailsDto errorDetailsDto = ErrorDetailsDto.builder()
				.timestamp(LocalDateTime.now())
				.message(ex.getMessage())
				.details(request.getDescription(false))
				.build();
		return new ResponseEntity<>(errorDetailsDto, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<?> badCredentialsException(BadCredentialsException ex, WebRequest request) {
		ErrorDetailsDto errorDetailsDto = ErrorDetailsDto.builder()
				.timestamp(LocalDateTime.now())
				.message(ex.getMessage())
				.details(request.getDescription(false))
				.build();
		return new ResponseEntity<>(errorDetailsDto, HttpStatus.UNAUTHORIZED);
	}
}
