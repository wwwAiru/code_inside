package ru.golikov.notes.security.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.golikov.notes.dto.UserDto;
import ru.golikov.notes.security.jwt.JwtTokenProvider;
import ru.golikov.notes.service.UserService;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    @PostMapping("/auth/register")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        userService.createUser(userDto);
        return new ResponseEntity<>("User created", HttpStatus.OK);
    }
}
