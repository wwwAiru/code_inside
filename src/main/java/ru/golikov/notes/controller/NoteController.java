package ru.golikov.notes.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.golikov.notes.domain.audit.service.AuditService;
import ru.golikov.notes.domain.note.dto.NoteDto;
import ru.golikov.notes.domain.note.service.NoteService;
import ru.golikov.notes.domain.security.model.UserDetailsImpl;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/notes")
public class NoteController {

    private final NoteService noteService;

    private final AuditService auditService;

    @ApiOperation(value = "Создать заметку", notes = "Возвращает созданную заметку")
    @PostMapping(value = "/create", consumes="application/json")
    public ResponseEntity<NoteDto> createNote(@RequestBody NoteDto noteDto,
                                              @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(noteService.createNote(noteDto, userDetails), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Найти заметку по id", notes = "Возвращает заметку")
    @GetMapping("/note")
    public ResponseEntity<NoteDto> findById(@RequestParam Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(noteService.findById(id, userDetails), HttpStatus.OK);
    }

    @ApiOperation(value = "Получить список всех заметок постранично", notes = "возвращает список заметок и объект пагинации")
    @GetMapping("/all")
    public ResponseEntity<Page<NoteDto>> getAllUserNotes(@RequestParam Integer page,
                                                         @RequestParam Integer size,
                                                         @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Page<NoteDto> allUserNotes = noteService.getAllUserNotes(userDetails, page, size);
        return new ResponseEntity<>(allUserNotes, HttpStatus.OK);
    }

    @ApiOperation(value = "Отредактировать заметку", notes = "Возвращает отредактированную заметку")
    @PutMapping(value = "/edit", consumes="application/json")
    public ResponseEntity<NoteDto> editNote(@RequestBody NoteDto noteDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        noteDto.setUserId(userDetails.getId());
        return new ResponseEntity<>(noteService.updateNote(noteDto), HttpStatus.OK);
    }

    @ApiOperation(value = "Удалить заметку", notes = "Возвращает строку с описанием, какая заметка удалена")
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteNote(@RequestParam Long id, @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        noteService.deleteNote(id, userDetails);
        return new ResponseEntity<>(String.format("Note with id = %d deleted", id), HttpStatus.OK);
    }

    @ApiOperation(value = "История изменений заметки", notes = "Возвращает ревизии заметки по её id")
    @GetMapping("/audit")
    public ResponseEntity<List<?>> getNotesAudit(@RequestParam Long id,
                                                 @RequestParam boolean fetchChanges,
                                                 @ApiIgnore @AuthenticationPrincipal UserDetailsImpl userDetails) {
        List<?> notesRevById = auditService.getNotesRevById(id, fetchChanges, userDetails);
        return new ResponseEntity<>(notesRevById, HttpStatus.OK);
    }
}
