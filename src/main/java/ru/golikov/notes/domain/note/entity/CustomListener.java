package ru.golikov.notes.domain.note.entity;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;


public class CustomListener implements RevisionListener {

    private static final String GUEST = "anonymousUser";

    public void newRevision(Object revisionEntity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomRevisionEntity exampleRevEntity = (CustomRevisionEntity) revisionEntity;
        if (!authentication.getName().contains(GUEST)) {
            exampleRevEntity.setCreatedBy(authentication.getName());
            exampleRevEntity.setModifiedBy(authentication.getName());
        } else {
            exampleRevEntity.setCreatedBy("anonymousUser");
        }
    }
}