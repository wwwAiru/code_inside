package ru.golikov.notes.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.golikov.notes.dto.UserDto;
import ru.golikov.notes.entity.Role;
import ru.golikov.notes.entity.User;
import ru.golikov.notes.error.exception.ResourceNotFoundException;
import ru.golikov.notes.repository.RoleRepository;
import ru.golikov.notes.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public UserDto createUser(UserDto userDto) {
        Role role = roleRepository.findByRole("USER").get();
        List<Role> roleEntities = new ArrayList<>();
        roleEntities.add(role);
        User user = new User();
        user.setActive(true);
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setMiddleName(userDto.getMiddleName());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setRoles(roleEntities);
        user.setCreateAt(LocalDateTime.now());
        user.setUpdateAt(LocalDateTime.now());
        userRepository.save(user);
        log.info("User {} created", userDto.getEmail());
        return userDto;
    }

    public User findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(String.format("user with id = %d not found", id)));
        if (user == null) {
            log.warn(String.format("user with id = %d not found", id));
        }
        return user;
    }

    public User findByEmail(String email) {
        User user = userRepository.findAllByEmail(email).orElseThrow(() -> new ResourceNotFoundException(String.format("user with email = %s not found", email)));
        if (user == null) {
            log.warn(String.format("user with email = %s not found", email));
        }
        return user;
    }

    public User updateUserPassword(UserDto userDto) {
        User user = userRepository.findAllByEmail(userDto.getEmail()).orElseThrow(() -> new ResourceNotFoundException(String.format("user with email = %s not found", userDto.getEmail())));
        if (user == null) {
            log.warn(String.format("user with email = %s not found", userDto.getEmail()));
        } else {
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            userRepository.save(user);
            log.info(String.format("user %s update password", user.getEmail()));
        }
        return user;
    }


    public void deleteUser(Long id) {
        userRepository.deleteById(id);
        log.info(String.format("user with id = %d deleted", id));
    }
}
