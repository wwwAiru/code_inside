package ru.golikov.notes.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private Long id;

    @JsonProperty("first_name")
    @NotBlank(message = "Обязательное поле")
    @Size(min = 1, max = 100, message = "Введите имя минимум 1 символ, максимум 100")
    private String firstName;

    @JsonProperty("last_name")
    @NotBlank(message = "Обязательное поле")
    @Size(min = 1, max = 100, message = "Введите фамилию минимум 1 символ, максимум 100")
    private String lastName;

    @JsonProperty("middle_name")
    @NotBlank(message = "Обязательное поле")
    @Size(min = 1, max = 100, message = "Введите отчество минимум 1 символ, максимум 100")
    private String middleName;

    @NotBlank(message = "Обязательное поле")
    @Email(message = "Введите действительный адрес электронной почты")
    private String email;

    @NotNull(message = "Обязательное поле")
    @Size(min = 8, max = 16, message = "Длина пароля должна быть не менее 8 и не более 16 символов")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @JsonProperty("create_at")
    private LocalDateTime createAt;

    @JsonProperty("update_at")
    private LocalDateTime updateAt;

    @JsonProperty("is_active")
    private boolean isActive;

    private List<String> roles;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return isActive == userDto.isActive && id.equals(userDto.id) && firstName.equals(userDto.firstName) && lastName.equals(userDto.lastName) && middleName.equals(userDto.middleName) && email.equals(userDto.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, middleName, email, isActive);
    }
}
