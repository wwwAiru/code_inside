package ru.golikov.notes.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ru.golikov.notes.AbstractSpringBootTest;
import ru.golikov.notes.domain.error.exception.NotFoundException;
import ru.golikov.notes.domain.note.dto.NoteDto;
import ru.golikov.notes.domain.note.entity.Note;
import ru.golikov.notes.domain.note.repository.NoteRepository;
import ru.golikov.notes.domain.note.service.NoteService;
import ru.golikov.notes.domain.security.model.UserDetailsImpl;
import ru.golikov.notes.domain.user.entity.User;
import ru.golikov.notes.util.NoteMapper;
import ru.golikov.notes.util.TestUtil;
import ru.golikov.notes.util.UserMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class NoteServiceTest extends AbstractSpringBootTest {

    @Autowired
    private NoteService noteService;

    @MockBean
    private NoteRepository noteRepository;

    @Test
    void createNote() {
        LocalDateTime localDateTime = LocalDateTime.now();
        UserDetailsImpl userDetails = TestUtil.createUserDetails();
        User user = UserMapper.toUser(userDetails);
        Note note = new Note(null, "title", "body", null, null, any());
        Note saved = new Note(1L, "title", "body", localDateTime, localDateTime, user);
        NoteDto expectedNoteDto = NoteDto.builder().id(1L).title("title").body("body").createAt(localDateTime)
                .updateAt(localDateTime).email("test@user.ru").userId(1L).build();
        when(noteRepository.save(note)).thenReturn(saved);
        assertThat(noteService.createNote(NoteDto.builder().title("title").body("body").build(),
                userDetails)).isEqualTo(expectedNoteDto);
    }

    @Test
    void findById() {
        Long noteId = 1L;
        UserDetailsImpl userDetails = TestUtil.createUserDetails();
        User user = UserMapper.toUser(userDetails);
        Note found = new Note(1L, "title", "body", any(), any(), user);
        Optional<Note> noteOptional = Optional.of(found);
        when(noteRepository.findByIdAndUserId(noteId, userDetails.getId())).thenReturn(noteOptional);
        assertThat(noteService.findById(noteId, userDetails))
                .isEqualTo(NoteDto.builder()
                        .id(1L)
                        .title("title")
                        .body("body")
                        .email("test@user.ru")
                        .userId(1L)
                        .build());
    }

    @Test
    void findById_NotFoundException() {
        Long noteId = 555L;
        UserDetailsImpl userDetails = TestUtil.createUserDetails();
        when(noteRepository.findByIdAndUserId(noteId, userDetails.getId()))
                .thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class,
                () -> noteService.findById(noteId, userDetails));
        assertEquals("Note with id = 555 not found", exception.getMessage());
    }

    @Test
    void getAllUserNotes() {
        Integer page = 0;
        Integer size = 5;
        UserDetailsImpl userDetails = TestUtil.createUserDetails();
        List<Note> noteList = TestUtil.creatNoteList();
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Note> notePage = new PageImpl<>(noteList, pageRequest, 5);
        when(noteRepository.findAllByUser(any(User.class), any(Pageable.class))).thenReturn(notePage);
        assertThat(noteService.getAllUserNotes(userDetails, page, size).getTotalElements()).isLessThan(6);
        assertThat(noteService.getAllUserNotes(userDetails, page, size).getTotalPages()).isEqualTo(1);
        List<NoteDto> noteDtoList = NoteMapper.toListDto(noteList);
        assertThat(noteService.getAllUserNotes(userDetails, page, size).toList()).isEqualTo(noteDtoList);
    }

    @Test
    void updateNote() {
        UserDetailsImpl userDetails = TestUtil.createUserDetails();
        Note note = TestUtil.createNote();
        NoteDto noteDtoBefore = NoteMapper.toDto(note);
        when(noteRepository.findByIdAndUserId(noteDtoBefore.getId(), noteDtoBefore.getUserId())).thenReturn(Optional.of(note));
        note.setBody("New Body");
        note.setUpdateAt(LocalDateTime.now());
        when(noteRepository.save(note)).thenReturn(note);
        NoteDto noteDto = NoteDto.builder()
                .id(1L)
                .title("title")
                .email(userDetails.getEmail())
                .userId(userDetails.getId())
                .body("New body")
                .build();
        assertThat(noteService.updateNote(noteDto)).isEqualTo(noteDto);
    }

    @Test
    void updateNote_NotFoundException() {
        NoteDto noteDto = NoteMapper.toDto(TestUtil.createNote());
        UserDetailsImpl userDetails = TestUtil.createUserDetails();
        when(noteRepository.findByIdAndUserId(noteDto.getId(), userDetails.getId()))
                .thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class, () -> noteService.updateNote(noteDto));
        assertEquals("Note with id = 1 not found", exception.getMessage());
    }

    @Test
    void deleteNote() {
        Note note = TestUtil.createNote();
        NoteDto noteDto = NoteMapper.toDto(TestUtil.createNote());
        UserDetailsImpl userDetails = TestUtil.createUserDetails();
        when(noteRepository.findByIdAndUserId(noteDto.getId(), userDetails.getId())).thenReturn(Optional.of(note));
        doNothing().when(noteRepository).deleteById(1L);
        assertThat(noteService.deleteNote(1L, userDetails)).isEqualTo("note with id = 1, userId = 1 deleted");
    }

    @Test
    void deleteNote_NotFoundException() {
        NoteDto noteDto = NoteMapper.toDto(TestUtil.createNote());
        UserDetailsImpl userDetails = TestUtil.createUserDetails();
        when(noteRepository.findByIdAndUserId(noteDto.getId(), userDetails.getId())).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class, () -> noteService.deleteNote(1L, userDetails));
        assertEquals("Cant delete note with id = 1, userId = 1, note not found", exception.getMessage());
    }
}