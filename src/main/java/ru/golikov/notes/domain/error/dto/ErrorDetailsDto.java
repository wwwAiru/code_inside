package ru.golikov.notes.domain.error.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ErrorDetailsDto {

	@JsonFormat(pattern = "dd-MM-yyyy HH:mm a")
	private LocalDateTime timestamp;

	private String message;

	private String details;
}
