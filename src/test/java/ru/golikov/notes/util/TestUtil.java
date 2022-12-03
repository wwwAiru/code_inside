package ru.golikov.notes.util;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.golikov.notes.domain.note.entity.Note;
import ru.golikov.notes.domain.security.model.UserDetailsImpl;
import ru.golikov.notes.domain.user.entity.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TestUtil {

    public static UserDetailsImpl createUserDetails(){
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return UserDetailsImpl.builder()
                .id(1L)
                .createAt(null)
                .updateAt(null)
                .firstName("Test")
                .lastName("User")
                .middleName("Userovich")
                .email("test@user.ru")
                .password(null)
                .isActive(true)
                .authorities(authorities)
                .build();
    }

    public static Note createNoteByParams(Long id, String title, String body,
                                       LocalDateTime createAt, LocalDateTime updateAt, User user) {
        return new Note(id, title, body, createAt, updateAt, user);
    }

    public static Note createNote() {
        return createNoteByParams(1L, "title", "body", null,
                null, UserMapper.toUser(createUserDetails())
        );
    }

    public static List<Note> creatNoteList() {
        List<Note> noteList = new ArrayList<>();
        User user = UserMapper.toUser(createUserDetails());
        IntStream.range(0, 5).forEach(i -> noteList.add(createNoteByParams((long) i, "title"+i, "body"+i,
                LocalDateTime.now(), LocalDateTime.now(), user)));
        return noteList;
    }
}
