package ru.golikov.notes.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.golikov.notes.domain.note.dto.NoteDto;
import ru.golikov.notes.domain.note.service.NoteService;
import ru.golikov.notes.domain.security.model.UserDetailsImpl;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;

    @PostMapping("/create")
    public ResponseEntity<NoteDto> createNote(@RequestBody NoteDto noteDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        NoteDto note = noteService.createNote(noteDto, userDetails);
        return new ResponseEntity<>(note, HttpStatus.CREATED);
    }
}
