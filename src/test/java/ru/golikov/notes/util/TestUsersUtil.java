package ru.golikov.notes.util;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.golikov.notes.domain.security.model.UserDetailsImpl;

import java.util.ArrayList;
import java.util.Collection;

public class TestUsersUtil {

    public static UserDetailsImpl getUserDetails(){
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
}
