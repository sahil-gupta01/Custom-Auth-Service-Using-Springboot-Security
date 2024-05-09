package com.auth.customauthservice.dtos;

import com.auth.customauthservice.models.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserResponseDto {

    private List<Role> roles;
    private String email;

}
