package ru.golikov.notes.domain.note.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.golikov.notes.domain.note.entity.Note;
import ru.golikov.notes.domain.user.entity.User;

import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {
    Page<Note> findAllByUser(User user, Pageable pageable);

    Optional<Note> findByIdAndUser(Long id, User user);
}
