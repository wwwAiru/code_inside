package ru.golikov.notes.domain.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import ru.golikov.notes.domain.error.exception.NotFoundException;
import ru.golikov.notes.domain.error.exception.UserRegistrationException;
import ru.golikov.notes.domain.role.entity.Role;
import ru.golikov.notes.domain.role.repository.RoleRepository;
import ru.golikov.notes.domain.user.dto.UserDto;
import ru.golikov.notes.domain.user.entity.User;
import ru.golikov.notes.domain.user.repository.UserRepository;
import ru.golikov.notes.domain.util.UserMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public void createUser(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail());
        if (user == null) {
            User newUser = new User();
            Role role = roleRepository.findByRole("ROLE_USER").get();
            List<Role> roleEntities = new ArrayList<>();
            roleEntities.add(role);
            newUser.setActive(true);
            newUser.setEmail(userDto.getEmail());
            newUser.setFirstName(userDto.getFirstName());
            newUser.setLastName(userDto.getLastName());
            newUser.setMiddleName(userDto.getMiddleName());
            newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
            newUser.setRoles(roleEntities);
            newUser.setCreateAt(LocalDateTime.now());
            newUser.setUpdateAt(LocalDateTime.now());
            userRepository.save(newUser);
            log.info("User {} created", userDto.getEmail());
        } else {
            throw new UserRegistrationException(String.format("User with email: %s already exists", userDto.getEmail()));
        }
    }

    public UserDto editUser(UserDto userDto) {
        List<Role> roles = userDto.getRoles().stream()
                .map(roleRepository::findByRole)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        User user = findById(userDto.getId());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setActive(userDto.isActive());
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setMiddleName(userDto.getMiddleName());
        user.setUpdateAt(LocalDateTime.now());
        if(!CollectionUtils.isEmpty(roles)) {
            user.setRoles(roles);
        }
        User savedUser = userRepository.save(user);
        return UserMapper.toDto(savedUser);
    }

    public List<UserDto> findAllUsers() {
        return UserMapper.toDtoList(userRepository.findAll());
    }

    public User findById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new NotFoundException(String.format("user with id = %d not found", id)));
        if (user == null) {
            log.warn(String.format("user with id = %d not found", id));
        }
        return user;
    }

    public User findByEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            log.warn(String.format("user with email = %s not found", email));
            throw new NotFoundException(String.format("user with email = %s not found", email));
        }
        return user;
    }

    public User updateUserPassword(UserDto userDto) {
        User user = userRepository.findByEmail(userDto.getEmail());
        if (user == null) {
            log.warn(String.format("user with email = %s not found", userDto.getEmail()));
            throw new NotFoundException(String.format("user with email = %s not found", userDto.getEmail()));
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
