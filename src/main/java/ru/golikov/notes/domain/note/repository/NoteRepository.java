package ru.golikov.notes.domain.note.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.golikov.notes.domain.note.entity.Note;

public interface NoteRepository extends JpaRepository<Note, Long> {
}
