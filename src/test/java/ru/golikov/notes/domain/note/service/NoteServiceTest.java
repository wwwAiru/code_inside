package ru.golikov.notes.domain.note.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.golikov.notes.AbstractSpringBootTest;
import ru.golikov.notes.domain.error.exception.NotFoundException;
import ru.golikov.notes.domain.note.dto.NoteDto;
import ru.golikov.notes.domain.note.entity.Note;
import ru.golikov.notes.domain.note.repository.NoteRepository;
import ru.golikov.notes.domain.security.model.UserDetailsImpl;
import ru.golikov.notes.domain.user.entity.User;
import ru.golikov.notes.util.TestUsersUtil;
import ru.golikov.notes.util.UserMapper;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class NoteServiceTest extends AbstractSpringBootTest {

    @Autowired
    private NoteService noteService;

    @MockBean
    private NoteRepository noteRepository;

    @Test
    void createNote() {
        LocalDateTime localDateTime = LocalDateTime.now();
        UserDetailsImpl userDetails = TestUsersUtil.getUserDetails();
        User user = UserMapper.toUser(userDetails);
        Note note = new Note(null, "title", "body", null, null, any());
        Note saved = new Note(1L, "title", "body", localDateTime, localDateTime, user);
        when(noteRepository.save(note)).thenReturn(saved);
        assertThat(noteService.createNote(NoteDto.builder().title("title").body("body").build(),
                userDetails)).isEqualTo(NoteDto.builder().id(1L).title("title").body("body").build());
    }

    @Test
    void findById() {
        Long noteId = 1L;
        UserDetailsImpl userDetails = TestUsersUtil.getUserDetails();
        User user = UserMapper.toUser(userDetails);
        Note found = new Note(1L, "title", "body", any(), any(), user);
        Optional<Note> noteOptional = Optional.of(found);
        when(noteRepository.findByIdAndUserId(noteId, userDetails.getId())).thenReturn(noteOptional);
        assertThat(noteService.findById(noteId, userDetails))
                .isEqualTo(NoteDto.builder().id(1L).title("title").body("body").build());
    }
    @Test
    void findById_NotFoundException() {
        Long noteId = 555L;
        UserDetailsImpl userDetails = TestUsersUtil.getUserDetails();
        when(noteRepository.findByIdAndUserId(noteId, userDetails.getId()))
                .thenReturn(Optional.of(new Note()));
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> noteService.findById(noteId, userDetails));
        assertEquals("Note with id = 555 not found", exception.getMessage());
    }

//    @Test
//    void getAllUserNotes() {
//        int page = 1;
//        int size = 1;
//        UserDetailsImpl userDetails = TestUsersUtil.getUserDetails();
//        User user = UserMapper.toUser(userDetails);
//        Page<Note> pageableNotes = null;
//        when(noteRepository.findAllByUser(user, PageRequest.of(page, size))).thenReturn(pageableNotes);
//    }

    @Test
    void updateNote() {
    }

    @Test
    void deleteNote() {
    }
}