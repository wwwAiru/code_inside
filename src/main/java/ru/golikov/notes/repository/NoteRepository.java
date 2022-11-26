package ru.golikov.notes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.golikov.notes.entity.Note;

public interface NoteRepository extends JpaRepository<Note, Long> {
}
