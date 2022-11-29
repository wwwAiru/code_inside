package ru.golikov.notes.domain.note.service;

import liquibase.repackaged.org.apache.commons.lang3.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.golikov.notes.domain.error.exception.NotFoundException;
import ru.golikov.notes.domain.note.dto.NoteDto;
import ru.golikov.notes.domain.note.entity.Note;
import ru.golikov.notes.domain.note.repository.NoteRepository;
import ru.golikov.notes.domain.security.model.UserDetailsImpl;
import ru.golikov.notes.util.NoteMapper;
import ru.golikov.notes.util.UserMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
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

    public NoteDto editNote(NoteDto noteDto) {
        Optional<Note> noteOpt = noteRepository.findById(noteDto.getId());
        if (noteOpt.isPresent()) {
            Note note = noteOpt.get();
            if (!StringUtils.isEmpty(noteDto.getTitle())) {
                note.setTitle(noteDto.getTitle());
            }
            if (!StringUtils.isEmpty(noteDto.getBody())) {
                note.setBody(noteDto.getBody());
            }
            note.setUpdateAt(LocalDateTime.now());
            Note savedNote = noteRepository.save(note);
            return NoteMapper.toDto(savedNote);
        } else {
            log.warn("Note with id = {} not found", noteDto.getId());
            throw new NotFoundException(String.format("Note with id = %d not found", noteDto.getId()));
        }
    }

    public void deleteNote(Long id, UserDetailsImpl userDetails) {
        Optional<Note> note = noteRepository.findByIdAndUser(id, UserMapper.toUser(userDetails));
        if (note.isPresent()) {
            noteRepository.deleteById(id);
            log.info(String.format("note with id = %d deleted", id));
        } else {
            log.warn(String.format("Can't delete note with id = %d, note not found", id));
            throw new NotFoundException(String.format("Cant delete note with id = %d, note not found", id));
        }
    }
}
