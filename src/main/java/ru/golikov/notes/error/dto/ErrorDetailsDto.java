package ru.golikov.notes.error.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class ErrorDetailsDto {

	private Date timestamp;

	private String message;

	private String details;
}
