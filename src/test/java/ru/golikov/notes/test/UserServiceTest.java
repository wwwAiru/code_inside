package ru.golikov.notes.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.golikov.notes.AbstractSpringBootTest;
import ru.golikov.notes.domain.error.exception.NotFoundException;
import ru.golikov.notes.domain.error.exception.UserRegistrationException;
import ru.golikov.notes.domain.role.entity.Role;
import ru.golikov.notes.domain.role.repository.RoleRepository;
import ru.golikov.notes.domain.user.dto.UserDto;
import ru.golikov.notes.domain.user.entity.User;
import ru.golikov.notes.domain.user.repository.UserRepository;
import ru.golikov.notes.domain.user.service.UserService;
import ru.golikov.notes.util.TestUtil;
import ru.golikov.notes.util.UserMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest extends AbstractSpringBootTest {

    @Autowired
    private UserService userService;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserRepository userRepository;

    @Test
    void createUserTest() {
        LocalDateTime dateTime = LocalDateTime.now();
        Role roleUser = TestUtil.createRoleUser();
        UserDto createUserDto = UserDto.builder().firstName("Test").lastName("User").middleName("Userovich")
                .email("test@user.ru").password("11111111").build();
        User savedUser = new User(1L, "Test", "User", "Userovich", "test@user.ru",
                "encodedPass", dateTime, dateTime, true, null, new ArrayList<>());
        savedUser.addRole(roleUser);
        when(userRepository.findByEmail(createUserDto.getEmail())).thenReturn(Optional.empty());
        when(roleRepository.findByRole("ROLE_USER")).thenReturn(Optional.of(roleUser));
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        UserDto expectedUserDto = TestUtil.createUserDto();
        assertThat(userService.createUser(createUserDto)).isEqualTo(expectedUserDto);
    }

    @Test
    void createUser_AlreadyExistTest() {
        UserDto userDto = TestUtil.createUserDto();
        Role roleUser = TestUtil.createRoleUser();
        User user = new User(1L, "Test", "User", "Userovich", "test@user.ru",
                "encodedPass", null, null, true, null, new ArrayList<>());
        user.addRole(roleUser);
        when(userRepository.findByEmail(userDto.getEmail())).thenReturn(Optional.of(user));
        UserRegistrationException exception = assertThrows(UserRegistrationException.class, () -> userService.createUser(userDto));
        assertEquals("User with email: test@user.ru already exists", exception.getMessage());
    }

    @Test
    void editUserTest() {
        UserDto userDto = TestUtil.createUserDto();
        Role roleUser = TestUtil.createRoleUser();
        User user = new User(1L, "Test", "User", "Userovich", "test@user.ru",
                "encodedPass", null, null, true, null, new ArrayList<>());
        User editedUser = new User(1L, "New Name", "New LastName", "Userovich", "test@user.ru",
                "encodedPass", null, null, true, null, new ArrayList<>());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(editedUser);
        when(roleRepository.findByRole("ROLE_USER")).thenReturn(Optional.of(roleUser));
        UserDto expectedUserDto = TestUtil.createUserDto();
        expectedUserDto.setFirstName("New Name");
        expectedUserDto.setLastName("New LastName");
        assertThat(userService.editUser(userDto)).isEqualTo(expectedUserDto);
    }

    @Test
    void editUser_NotFoundTest() {
        UserDto userDto = TestUtil.createUserDto();
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.editUser(userDto));
        assertEquals("user with id = 1 not found", exception.getMessage());
    }

    @Test
    void findByIdTest() {
        User user = new User(1L, "Test", "User", "Userovich", "test@user.ru",
                "encodedPass", null, null, true, null, new ArrayList<>());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User expectedUser = UserMapper.toUser(TestUtil.createUserDto());
        assertThat(userService.findById(1L)).isEqualTo(expectedUser);
    }

    @Test
    void findById_NotFoundTest() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.findById(1L));
        assertEquals("user with id = 1 not found", exception.getMessage());
    }

    @Test
    void findByEmailTest() {
        User user = new User(1L, "Test", "User", "Userovich", "test@user.ru",
                "encodedPass", null, null, true, null, new ArrayList<>());
        when(userRepository.findByEmail("test@user.ru")).thenReturn(Optional.of(user));
        User expectedUser = UserMapper.toUser(TestUtil.createUserDto());
        assertThat(userService.findByEmail("test@user.ru")).isEqualTo(expectedUser);
    }

    @Test
    void findByEmail_NotFoundTest() {
        when(userRepository.findByEmail("test@user.ru")).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.findByEmail("test@user.ru"));
        assertEquals("user with email = test@user.ru not found", exception.getMessage());
    }

    @Test
    void deleteUserTest() {
        doNothing().when(userRepository).deleteById(1L);
        assertThat(userService.deleteUser(1L)).isEqualTo("user with id = 1 deleted");
    }

    @Test
    void deleteUser_NotFoundTest() {
        doThrow(NotFoundException.class).when(userRepository).deleteById(1L);
        NotFoundException exception = assertThrows(NotFoundException.class, () -> userService.deleteUser(1L));
        assertEquals("can't delete user with id = 1, user not found", exception.getMessage());
    }
}