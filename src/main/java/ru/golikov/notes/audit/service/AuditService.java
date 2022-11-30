package ru.golikov.notes.audit.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.stereotype.Service;
import ru.golikov.notes.domain.error.exception.NotFoundException;
import ru.golikov.notes.domain.note.entity.Note;
import ru.golikov.notes.domain.security.model.UserDetailsImpl;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditReader auditReader;

    public List<?> getNotesRevById(Long id, boolean fetchChanges, UserDetailsImpl userDetails) {
        AuditQuery auditQuery;
        try {
            if (fetchChanges) {
                auditQuery = auditReader.createQuery()
                        .forRevisionsOfEntityWithChanges(Note.class, true);
            } else {
                auditQuery = auditReader.createQuery()
                        .forRevisionsOfEntity(Note.class, true);
            }
            auditQuery.add(AuditEntity.id().eq(id))
                    .add(AuditEntity.property("user_id").eq(userDetails.getId()));
            return auditQuery.getResultList();
        } catch (RuntimeException e) {
            log.warn("Revisions by {}, note id = {} not found", userDetails.getEmail(), id);
            throw new NotFoundException(String.format("Revisions by note id = %d not found", id));
        }
    }

}
