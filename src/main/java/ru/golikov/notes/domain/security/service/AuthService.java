package ru.golikov.notes.domain.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;
import ru.golikov.notes.domain.security.dto.ChangePasswordDto;
import ru.golikov.notes.domain.security.dto.LoginDto;
import ru.golikov.notes.domain.security.dto.TokenDto;
import ru.golikov.notes.domain.security.jwt.JwtTokenProvider;
import ru.golikov.notes.domain.security.model.UserDetailsImpl;
import ru.golikov.notes.domain.user.dto.UserDto;
import ru.golikov.notes.domain.user.service.UserService;
import ru.golikov.notes.util.UserMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    public TokenDto getToken(LoginDto loginDto) {
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
            return TokenDto.builder()
                    .email(loginDto.getEmail())
                    .token(jwtTokenProvider.createToken(UserMapper.toUser((UserDetailsImpl) authenticate.getPrincipal())))
                    .build();
        } catch (AuthenticationException e) {
            log.warn("Invalid email or password. User: {}", loginDto.getEmail());
            throw new BadCredentialsException("Invalid email or password");
        }
    }

    public void changePassword(ChangePasswordDto changePasswordDto) {
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(changePasswordDto.getEmail(), changePasswordDto.getPassword()));
            UserDetailsImpl principal = (UserDetailsImpl) authenticate.getPrincipal();
            UserDto userDto = UserMapper.toDto(principal);
            userDto.setPassword(changePasswordDto.getNewPassword());
            userService.editUser(userDto);
            log.info("Password for User: {} changed", changePasswordDto.getEmail());
        } catch (AuthenticationException e) {
            log.warn("Invalid email or password. User: {}", changePasswordDto.getEmail());
            throw new BadCredentialsException("Invalid email or password");
        }
    }
}
