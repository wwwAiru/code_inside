package ru.golikov.notes.domain.note.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import ru.golikov.notes.domain.user.entity.User;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Table(name = "notes")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@EntityListeners(CustomListener.class)
@Audited
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
    private User user;

}
