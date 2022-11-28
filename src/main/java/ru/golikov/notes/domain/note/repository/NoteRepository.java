package ru.golikov.notes.domain.note.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.golikov.notes.domain.note.entity.Note;
import ru.golikov.notes.domain.user.entity.User;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note, Long> {
    List<Note> findAllByUser(User user);
}
