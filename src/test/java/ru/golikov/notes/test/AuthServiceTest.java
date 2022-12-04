package ru.golikov.notes.test;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import ru.golikov.notes.AbstractSpringBootTest;
import ru.golikov.notes.domain.security.dto.ChangePasswordDto;
import ru.golikov.notes.domain.security.dto.LoginDto;
import ru.golikov.notes.domain.security.model.UserDetailsImpl;
import ru.golikov.notes.domain.security.service.AuthService;
import ru.golikov.notes.domain.user.dto.UserDto;
import ru.golikov.notes.domain.user.service.UserService;
import ru.golikov.notes.util.TestUtil;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthServiceTest extends AbstractSpringBootTest {

    @Autowired
    private AuthService authService;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private Authentication authentication;

    @Test
    void getTokenTest() {
        UserDetailsImpl userDetails = TestUtil.createUserDetails();
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("test@user.ru");
        loginDto.setPassword("testPassword");
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getAuthorities());
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(auth);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        assertThat(authService.getToken(loginDto)).isNotNull();
        assertThat(authService.getToken(loginDto).getToken()).isNotEmpty();
        assertThat(authService.getToken(loginDto).getEmail()).isEqualTo("test@user.ru");
    }

    @Test
    void getToken_AuthenticationExceptionTest() {
        UserDetailsImpl userDetails = TestUtil.createUserDetails();
        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("test@user.ru");
        loginDto.setPassword("testPassword");
        when(authenticationManager.authenticate(any(Authentication.class))).thenThrow(BadCredentialsException.class);
        BadCredentialsException exception = assertThrows(BadCredentialsException.class, () -> authService.getToken(loginDto));
        assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    void changePasswordTest() {
        UserDto userDto = TestUtil.createUserDto();
        ChangePasswordDto changePasswordDto = new ChangePasswordDto();
        changePasswordDto.setEmail(userDto.getEmail());
        changePasswordDto.setPassword("currentPassword");
        changePasswordDto.setNewPassword("newPassword");
        UserDetailsImpl userDetails = TestUtil.createUserDetails();
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getAuthorities());
        when(authenticationManager.authenticate(any(Authentication.class))).thenReturn(auth);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userService.editUser(userDto)).thenReturn(userDto);
        assertThat(authService.changePassword(changePasswordDto)).isNotEmpty();
        assertThat(authService.changePassword(changePasswordDto)).isEqualTo("Password for User: test@user.ru changed");
    }
}