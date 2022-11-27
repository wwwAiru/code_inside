package ru.golikov.notes.domain.note.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class NoteDto {

    private Long id;

    private String title;

    private String body;

    @JsonProperty("create_at")
    private LocalDateTime createAt;

    @JsonProperty("update_at")
    private LocalDateTime updateAt;

    @JsonProperty("author")
    private String email;
}
