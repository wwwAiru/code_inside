package ru.golikov.notes.domain.note.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.golikov.notes.domain.error.exception.NotFoundException;
import ru.golikov.notes.domain.note.dto.NoteDto;
import ru.golikov.notes.domain.note.entity.Note;
import ru.golikov.notes.domain.note.repository.NoteRepository;
import ru.golikov.notes.domain.security.model.UserDetailsImpl;
import ru.golikov.notes.util.NoteMapper;
import ru.golikov.notes.util.UserMapper;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;

    private final CacheManager cacheManager;

    public NoteDto createNote(NoteDto noteDto, UserDetailsImpl userDetails) {
        Note note = new Note();
        note.setTitle(noteDto.getTitle());
        note.setBody(noteDto.getBody());
        note.setCreateAt(LocalDateTime.now());
        note.setUpdateAt(LocalDateTime.now());
        note.setUser(UserMapper.toUser(userDetails));
        Note savedNote = noteRepository.save(note);
        NoteDto savedNoteDto = NoteMapper.toDto(savedNote);
        Objects.requireNonNull(cacheManager.getCache("notes")).put(savedNoteDto, savedNoteDto);
        return savedNoteDto;
    }

    public NoteDto findById(Long id, UserDetailsImpl userDetails) {
        Optional<Note> note = noteRepository.findByIdAndUserId(id, userDetails.getId());
        if (note.isPresent()) {
            return NoteMapper.toDto(note.get());
        } else throw new NotFoundException(String.format("Note with id = %d not found", id));
    }

    public Page<NoteDto> getAllUserNotes(UserDetailsImpl userDetails, Integer page, Integer size) {
        return noteRepository.findAllByUser(UserMapper.toUser(userDetails), PageRequest.of(page, size))
                .map(NoteMapper::toDto);
    }

    @Cacheable("notes")
    @SneakyThrows
    public NoteDto updateNote(NoteDto noteDto) {
        Optional<Note> noteOpt = noteRepository.findByIdAndUserId(noteDto.getId(), noteDto.getUserId());
        if (noteOpt.isPresent()) {
            Note note = noteOpt.get();
            Objects.requireNonNull(cacheManager.getCache("notes")).evict(NoteMapper.toDto(note));
            note.setUpdateAt(LocalDateTime.now());
            note.setTitle(noteDto.getTitle());
            note.setBody(noteDto.getBody());
            Note savedNote = noteRepository.save(note);
            NoteDto savedNoteDto = NoteMapper.toDto(savedNote);
            Objects.requireNonNull(cacheManager.getCache("notes")).put(savedNoteDto, savedNoteDto);
            return savedNoteDto;
        } else {
            log.warn("Note with id = {} not found", noteDto.getId());
            throw new NotFoundException(String.format("Note with id = %d not found", noteDto.getId()));
        }
    }

    public void deleteNote(Long noteId, UserDetailsImpl userDetails) {
        Optional<Note> note = noteRepository.findByIdAndUserId(noteId, userDetails.getId());
        if (note.isPresent()) {
            noteRepository.deleteById(noteId);
            Objects.requireNonNull(cacheManager.getCache("notes")).evict(NoteMapper.toDto(note.get()));
            log.info(String.format("note with noteId = %d deleted", noteId));
        } else {
            log.warn(String.format("Can't delete note with noteId = %d, note not found", noteId));
            throw new NotFoundException(String.format("Cant delete note with noteId = %d, note not found", noteId));
        }
    }
}
