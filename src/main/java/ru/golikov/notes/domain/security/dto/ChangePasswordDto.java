package ru.golikov.notes.domain.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChangePasswordDto {

    private String email;

    private String password;

    @JsonProperty("new_password")
    private String newPassword;
}
