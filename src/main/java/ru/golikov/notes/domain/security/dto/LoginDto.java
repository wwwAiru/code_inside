package ru.golikov.notes.domain.security.dto;

import lombok.Data;

@Data
public class LoginDto {

    private String email;

    private String password;
}
