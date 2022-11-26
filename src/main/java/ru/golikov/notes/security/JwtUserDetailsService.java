package ru.golikov.notes.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.golikov.notes.entity.User;
import ru.golikov.notes.security.jwt.JwtUserFactory;
import ru.golikov.notes.service.UserService;

@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {

    private UserService userService;

    @Autowired
    public JwtUserDetailsService(@Lazy UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findByEmail(email);
        return JwtUserFactory.createJwtUser(user);
    }
}
