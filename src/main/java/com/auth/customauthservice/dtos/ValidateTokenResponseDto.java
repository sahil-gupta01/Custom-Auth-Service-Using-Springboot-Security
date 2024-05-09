package com.auth.customauthservice.dtos;

import com.auth.customauthservice.models.SessionStatus;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ValidateTokenResponseDto {

    private UserResponseDto userResponseDto;
    private SessionStatus sessionStatus;
}
