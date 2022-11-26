package ru.golikov.notes.domain.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.golikov.notes.domain.error.dto.ErrorDetailsDto;
import ru.golikov.notes.domain.error.exception.JwtAuthenticationException;
import ru.golikov.notes.domain.error.exception.NotFoundException;
import ru.golikov.notes.domain.error.exception.UserRegistrationException;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<?> notFoundException(NotFoundException ex, WebRequest request) {
		ErrorDetailsDto errorDetailsDto = ErrorDetailsDto.builder()
				.timestamp(new Date())
				.message(ex.getMessage())
				.details(request.getDescription(false))
				.build();
		return new ResponseEntity<>(errorDetailsDto, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(UserRegistrationException.class)
	public ResponseEntity<?> userRegistrationException(UserRegistrationException ex, WebRequest request) {
		ErrorDetailsDto errorDetailsDto = ErrorDetailsDto.builder()
				.timestamp(new Date())
				.message(ex.getMessage())
				.details(request.getDescription(false))
				.build();
		return new ResponseEntity<>(errorDetailsDto, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> globalExceptionHandler(Exception ex, WebRequest request) {
		ErrorDetailsDto errorDetailsDto = ErrorDetailsDto.builder()
				.timestamp(new Date())
				.message(ex.getMessage())
				.details(request.getDescription(false))
				.build();
		return new ResponseEntity<>(errorDetailsDto, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<?> badCredentialsException(BadCredentialsException ex, WebRequest request) {
		ErrorDetailsDto errorDetailsDto = ErrorDetailsDto.builder()
				.timestamp(new Date())
				.message(ex.getMessage())
				.details(request.getDescription(false))
				.build();
		return new ResponseEntity<>(errorDetailsDto, HttpStatus.UNAUTHORIZED);
	}

	@ExceptionHandler(JwtAuthenticationException.class)
	public ResponseEntity<?> jwtAuthenticationException(JwtAuthenticationException ex, WebRequest request) {
		ErrorDetailsDto errorDetailsDto = ErrorDetailsDto.builder()
				.timestamp(new Date())
				.message(ex.getMessage())
				.details(request.getDescription(false))
				.build();
		return new ResponseEntity<>(errorDetailsDto, HttpStatus.FORBIDDEN);
	}
}
