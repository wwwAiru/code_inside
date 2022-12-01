package ru.golikov.notes.domain.note.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.golikov.notes.domain.user.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "notes")
@NoArgsConstructor
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Audited(withModifiedFlag = true)
public class Note {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "body")
    private String body;

    @Column(name = "create_at")
    @Audited
    private LocalDateTime createAt;

    @Column(name = "update_at")
    @Audited
    private LocalDateTime updateAt;

    @ManyToOne()
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnore
    private User user;

}
