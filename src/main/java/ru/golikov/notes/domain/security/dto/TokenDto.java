package ru.golikov.notes.domain.security.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TokenDto {

    private String email;

    private String token;
}
