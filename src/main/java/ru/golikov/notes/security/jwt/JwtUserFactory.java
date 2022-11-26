package ru.golikov.notes.security.jwt;

import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ru.golikov.notes.entity.Role;
import ru.golikov.notes.entity.User;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
public final class JwtUserFactory {

    public static JwtUser createJwtUser(User user) {
        return new JwtUser(user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getMiddleName(),
                user.getEmail(),
                user.getPassword(),
                user.getCreateAt(),
                user.getUpdateAt(),
                user.isActive(),
                toGrantedAuthority(user.getRoles()));
    }

    private static List<GrantedAuthority> toGrantedAuthority(List<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole()))
                .collect(Collectors.toList());
    }
}
