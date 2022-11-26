package ru.golikov.notes.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.golikov.notes.error.dto.ErrorDetailsDto;
import ru.golikov.notes.error.exception.ResourceNotFoundException;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<?> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
		ErrorDetailsDto errorDetailsDto = ErrorDetailsDto.builder()
				.timestamp(new Date())
				.message(ex.getMessage())
				.details(request.getDescription(false))
				.build();
		return new ResponseEntity<>(errorDetailsDto, HttpStatus.NOT_FOUND);
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
}
