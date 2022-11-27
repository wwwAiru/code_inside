package ru.golikov.notes.domain.util;

import ru.golikov.notes.domain.role.entity.Role;
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
}
