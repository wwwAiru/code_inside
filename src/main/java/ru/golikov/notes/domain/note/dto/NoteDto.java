package ru.golikov.notes.domain.note.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Objects;

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

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoteDto noteDto = (NoteDto) o;
        return id.equals(noteDto.id) && title.equals(noteDto.title) && body.equals(noteDto.body) && userId.equals(noteDto.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, body, userId);
    }
}
