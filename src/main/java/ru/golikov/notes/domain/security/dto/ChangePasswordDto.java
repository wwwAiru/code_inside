package ru.golikov.notes.domain.security.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ChangePasswordDto {

    @NotBlank(message = "Обязательное поле")
    @Email(message = "Введите действительный адрес электронной почты")
    private String email;

    @NotBlank(message = "Обязательное поле")
    @Size(min = 8, max = 16, message = "Длина пароля должна быть не менее 8 и не более 16 символов")
    private String password;

    @JsonProperty("new_password")
    @NotBlank(message = "Обязательное поле")
    @Size(min = 8, max = 16, message = "Длина пароля должна быть не менее 8 и не более 16 символов")
    private String newPassword;
}
