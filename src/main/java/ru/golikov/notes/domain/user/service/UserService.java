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
import ru.golikov.notes.util.UserMapper;

import java.time.LocalDateTime;
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

    public UserDto createUser(UserDto userDto) {
        Optional<User> user = userRepository.findByEmail(userDto.getEmail());
        if (user.isEmpty()) {
            User newUser = UserMapper.toUser(userDto);
            newUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
            newUser.setCreateAt(LocalDateTime.now());
            newUser.setUpdateAt(LocalDateTime.now());
            newUser.setActive(true);
            Optional<Role> roleUser = roleRepository.findByRole("ROLE_USER");
            roleUser.ifPresent(newUser::addRole);
            User savedUser = userRepository.save(newUser);
            log.info("User {} created", userDto.getEmail());
            return UserMapper.toDto(savedUser);
        } else {
            log.warn("User with email: {} already exists", userDto.getEmail());
            throw new UserRegistrationException(String.format("User with email: %s already exists", userDto.getEmail()));
        }
    }

    public UserDto editUser(UserDto userDto) {
        User user = findById(userDto.getId());
        List<String> stringRoles = userDto.getRoles();
        if (!CollectionUtils.isEmpty(stringRoles)) {
        List<Role> roles = stringRoles.stream()
                .map(roleRepository::findByRole)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
            if(!CollectionUtils.isEmpty(stringRoles)) {
                user.setRoles(roles);
            }
        }
        user.setActive(userDto.isActive());
        if (userDto.getPassword() != null) user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        if(userDto.getEmail() != null) user.setEmail(userDto.getEmail());
        if(userDto.getFirstName() != null) user.setFirstName(userDto.getFirstName());
        if(userDto.getLastName() != null) user.setLastName(userDto.getLastName());
        if(userDto.getMiddleName() != null) user.setMiddleName(userDto.getMiddleName());
        user.setUpdateAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        return UserMapper.toDto(savedUser);
    }

    public List<UserDto> findAllUsers() {
        return UserMapper.toDtoList(userRepository.findAll());
    }

    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            log.warn("user with id = {} not found", id);
            throw new NotFoundException(String.format("user with id = %d not found", id));
        }
        return user.get();
    }

    public User findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            log.warn("user with email = {} not found", email);
            throw new NotFoundException(String.format("user with email = %s not found", email));
        }
        return user.get();
    }

    public String deleteUser(Long id) {
        try {
            userRepository.deleteById(id);
        } catch (Exception e) {
            log.warn(String.format("can't delete user with id = %d, user not found", id));
            throw new NotFoundException(String.format("can't delete user with id = %d, user not found", id));
        }
        log.info("user with id = {} deleted", id);
        return String.format("user with id = %d deleted", id);
    }
}
