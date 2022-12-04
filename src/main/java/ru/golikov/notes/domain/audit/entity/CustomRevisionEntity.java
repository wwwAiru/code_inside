package ru.golikov.notes.domain.audit.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;
import ru.golikov.notes.domain.audit.config.CustomListener;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "custom_revision_entity")
@RevisionEntity(CustomListener.class)
@Getter
@Setter
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class CustomRevisionEntity {

    @Id
    @GeneratedValue
    @RevisionNumber
    @JsonProperty("revision_id")
    private Long id;

    @RevisionTimestamp
    private Date timestamp;

    @JsonProperty("created_by")
    private String createdBy;

    @JsonProperty("modified_by")
    private String modifiedBy;

}
