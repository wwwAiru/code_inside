package ru.golikov.notes.domain.note.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.golikov.notes.domain.note.dto.NoteDto;
import ru.golikov.notes.domain.note.entity.Note;
import ru.golikov.notes.domain.note.repository.NoteRepository;
import ru.golikov.notes.domain.security.model.UserDetailsImpl;
import ru.golikov.notes.util.NoteMapper;
import ru.golikov.notes.util.UserMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteDto createNote(NoteDto noteDto, UserDetailsImpl userDetails) {
        Note note = new Note();
        note.setTitle(noteDto.getTitle());
        note.setBody(noteDto.getBody());
        note.setCreateAt(LocalDateTime.now());
        note.setUpdateAt(LocalDateTime.now());
        note.setUser(UserMapper.toUser(userDetails));
        Note savedNote = noteRepository.save(note);
        return NoteMapper.toDto(savedNote);
    }

    public List<NoteDto> getAllUserNotes(UserDetailsImpl userDetails) {
        List<Note> allByUser = noteRepository.findAllByUser(UserMapper.toUser(userDetails));
        return NoteMapper.toListDto(allByUser);
    }
}
