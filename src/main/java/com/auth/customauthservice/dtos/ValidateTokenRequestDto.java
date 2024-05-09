package com.auth.customauthservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ValidateTokenRequestDto {
    private Long userId;
    private String token;
}
