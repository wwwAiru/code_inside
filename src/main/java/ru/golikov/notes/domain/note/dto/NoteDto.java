package ru.golikov.notes.domain.note.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class NoteDto {

    private Long id;

    private String title;

    private String body;

    @JsonProperty("create_at")
    private LocalDate createAt;

    @JsonProperty("update_at")
    private LocalDate updateAt;

    private Long userId;
}
