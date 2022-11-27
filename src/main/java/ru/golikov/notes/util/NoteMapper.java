package ru.golikov.notes.util;

import ru.golikov.notes.domain.note.dto.NoteDto;
import ru.golikov.notes.domain.note.entity.Note;

public class NoteMapper {
    public static NoteDto toDto(Note note) {
        return NoteDto.builder()
                .id(note.getId())
                .title(note.getTitle())
                .body(note.getBody())
                .createAt(note.getCreateAt())
                .updateAt(note.getUpdateAt())
                .email(note.getUser().getEmail())
                .build();
    }
}
