package ru.golikov.notes.domain.security.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.golikov.notes.domain.user.dto.UserDto;
import ru.golikov.notes.domain.user.service.UserService;
import ru.golikov.notes.domain.util.UserMapper;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @GetMapping("/users/all")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> allUsers = userService.findAllUsers();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<UserDto> getUserById(@RequestParam Long id) {
        return new ResponseEntity<>(UserMapper.toDto(userService.findById(id)), HttpStatus.OK);
    }

    @PutMapping("/user/edit")
    public ResponseEntity<UserDto> editUser(@RequestBody UserDto userDto) {
        UserDto editedUser = userService.editUser(userDto);
        return new ResponseEntity<>(editedUser, HttpStatus.OK);
    }

    @DeleteMapping("/user/delete")
    public ResponseEntity<?> deleteUser(@RequestParam Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }
}
