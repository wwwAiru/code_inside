package ru.golikov.notes.controller;

import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.golikov.notes.domain.note.dto.NoteDto;
import ru.golikov.notes.domain.note.entity.Note;
import ru.golikov.notes.domain.note.service.NoteService;
import ru.golikov.notes.domain.security.model.UserDetailsImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;

    private final AuditReader auditReader;

    @PostMapping("/create")
    public ResponseEntity<NoteDto> createNote(@RequestBody NoteDto noteDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        NoteDto note = noteService.createNote(noteDto, userDetails);
        return new ResponseEntity<>(note, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<NoteDto>> getAllUserNotes(@RequestParam Integer page,
                                                         @RequestParam Integer size,
                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Page<NoteDto> allUserNotes = noteService.getAllUserNotes(userDetails, page, size);
        return new ResponseEntity<>(allUserNotes, HttpStatus.OK);
    }

    @PutMapping("/edit")
    public ResponseEntity<NoteDto> editNote(@RequestBody NoteDto noteDto) {
        return new ResponseEntity<>(noteService.editNote(noteDto), HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteNote(@RequestParam Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        noteService.deleteNote(id, userDetails);
        return new ResponseEntity<>(String.format("Note with id = %d deleted", id), HttpStatus.OK);
    }

    @GetMapping("/audit")
    public ResponseEntity<List> getNotesAudit(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        AuditQuery auditQuery = auditReader.createQuery().forRevisionsOfEntity(Note.class, false, true)
                .add(AuditEntity.property("user_id").eq(userDetails.getId()))
                .addProjection(AuditEntity.revisionType())
                .addProjection(AuditEntity.id())
                .addProjection(AuditEntity.selectEntity(false));
        List resultList = auditQuery.getResultList();
        resultList.forEach(System.out::println);
        return new ResponseEntity<>(resultList, HttpStatus.OK);
    }
}
