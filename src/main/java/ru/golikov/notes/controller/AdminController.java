package ru.golikov.notes.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.golikov.notes.domain.user.dto.UserDto;
import ru.golikov.notes.domain.user.service.UserService;
import ru.golikov.notes.util.UserMapper;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @ApiOperation(value = "Получить список пользователей", notes = "Возвращает список пользователей")
    @GetMapping("/users/all")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> allUsers = userService.findAllUsers();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @ApiOperation(value = "Получить пользователя по id", notes = "Возвращает пользователя")
    @GetMapping("/user")
    public ResponseEntity<UserDto> getUserById(@RequestParam Long id) {
        return new ResponseEntity<>(UserMapper.toDto(userService.findById(id)), HttpStatus.OK);
    }

    @ApiOperation(value = "Изменить/редактировать пользователя", notes = "Возвращает измененного пользователя")
    @PutMapping("/user/edit")
    public ResponseEntity<UserDto> editUser(@RequestBody UserDto userDto) {
        UserDto editedUser = userService.editUser(userDto);
        return new ResponseEntity<>(editedUser, HttpStatus.OK);
    }

    @ApiOperation(value = "Удалить пользователя", notes = "Возвращает 'пользователь с id = {id} удалён'")
    @DeleteMapping("/user/delete")
    public ResponseEntity<?> deleteUser(@RequestParam Long id) {
        String resp = userService.deleteUser(id);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }
}
