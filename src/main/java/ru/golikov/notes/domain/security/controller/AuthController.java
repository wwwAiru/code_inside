package ru.golikov.notes.domain.security.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.golikov.notes.domain.security.dto.LoginDto;
import ru.golikov.notes.domain.security.dto.TokenDto;
import ru.golikov.notes.domain.security.service.AuthService;
import ru.golikov.notes.domain.user.dto.UserDto;
import ru.golikov.notes.domain.user.service.UserService;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final UserService userService;

    @PostMapping("/auth/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        userService.createUser(userDto);
        return new ResponseEntity<>(String.format("User %s created", userDto.getEmail()), HttpStatus.CREATED);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginDto loginDto) {
        return new ResponseEntity<>(authService.getToken(loginDto), HttpStatus.OK);
    }
}
