package ru.golikov.notes.domain.security.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import ru.golikov.notes.domain.security.dto.LoginDto;
import ru.golikov.notes.domain.security.dto.TokenDto;
import ru.golikov.notes.domain.security.jwt.JwtTokenProvider;
import ru.golikov.notes.domain.user.entity.User;
import ru.golikov.notes.domain.user.service.UserService;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    public TokenDto getToken(LoginDto loginDto) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
            User user = userService.findByEmail(loginDto.getEmail());
            return TokenDto.builder()
                    .email(loginDto.getEmail())
                    .token(jwtTokenProvider.createToken(user.getEmail(), user.getRoles()))
                    .build();
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid email or password");
        }
    }
}
