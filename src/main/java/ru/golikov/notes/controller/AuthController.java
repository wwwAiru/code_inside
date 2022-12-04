package ru.golikov.notes.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.golikov.notes.domain.security.dto.ChangePasswordDto;
import ru.golikov.notes.domain.security.dto.LoginDto;
import ru.golikov.notes.domain.security.dto.TokenDto;
import ru.golikov.notes.domain.security.service.AuthService;
import ru.golikov.notes.domain.user.dto.UserDto;
import ru.golikov.notes.domain.user.service.UserService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final UserService userService;

    @ApiOperation(value = "Зарегистрировать пользователя", notes = "Возвращает зарегистрированного пользователя")
    @PostMapping("/auth/register")
    public ResponseEntity<UserDto> registerUser(@Valid @RequestBody UserDto userDto) {
        UserDto user = userService.createUser(userDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Залогиниться (получить токен авторизации)", notes = "Возвращает токен авторизации")
    @PostMapping("/auth/login")
    public ResponseEntity<TokenDto> login(@Valid @RequestBody LoginDto loginDto) {
        return new ResponseEntity<>(authService.getToken(loginDto), HttpStatus.OK);
    }

    @ApiOperation(value = "Сменить пароль", notes = "При успешной смене пароля возвращает 'Password changed'")
    @PutMapping("/auth/password/change")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto) {
        String resp = authService.changePassword(changePasswordDto);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }
}
