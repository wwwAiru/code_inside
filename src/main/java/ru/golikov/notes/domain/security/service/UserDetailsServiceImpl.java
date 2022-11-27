package ru.golikov.notes.domain.security.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.golikov.notes.domain.security.model.UserDetailsImpl;
import ru.golikov.notes.domain.user.entity.User;
import ru.golikov.notes.domain.user.service.UserService;

import java.util.stream.Collectors;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserService userService;

    @Autowired
    public UserDetailsServiceImpl(@Lazy UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userService.findByEmail(email);
        return UserDetailsImpl.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .middleName(user.getMiddleName())
                .password(user.getPassword())
                .createAt(user.getCreateAt())
                .updateAt(user.getUpdateAt())
                .isActive(user.isActive())
                .authorities(user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getRole()))
                        .collect(Collectors.toList()))
                .build();
    }
}
