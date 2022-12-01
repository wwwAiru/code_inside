package ru.golikov.notes.domain.audit.config;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.golikov.notes.domain.audit.entity.CustomRevisionEntity;


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