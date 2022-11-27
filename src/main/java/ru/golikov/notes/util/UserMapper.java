package ru.golikov.notes.util;

import org.springframework.security.core.GrantedAuthority;
import ru.golikov.notes.domain.role.entity.Role;
import ru.golikov.notes.domain.security.model.UserDetailsImpl;
import ru.golikov.notes.domain.user.dto.UserDto;
import ru.golikov.notes.domain.user.entity.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserDto toDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .middleName(user.getMiddleName())
                .createAt(user.getCreateAt())
                .updateAt(user.getUpdateAt())
                .isActive(user.isActive())
                .roles(user.getRoles().stream().map(Role::getRole).collect(Collectors.toList()))
                .build();
    }

    public static List<UserDto> toDtoList(List<User> users) {
        return users.stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    public static UserDto toDto(UserDetailsImpl userDetails) {
        return UserDto.builder()
                .id(userDetails.getId())
                .email(userDetails.getEmail())
                .firstName(userDetails.getFirstName())
                .lastName(userDetails.getLastName())
                .middleName(userDetails.getMiddleName())
                .createAt(userDetails.getCreateAt())
                .updateAt(userDetails.getUpdateAt())
                .isActive(userDetails.isEnabled())
                .roles(userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .build();
    }

    public static User toUser(UserDetailsImpl userDetails) {
        User user = new User();
        user.setId(userDetails.getId());
        user.setEmail(userDetails.getEmail());
        return user;
    }
}
