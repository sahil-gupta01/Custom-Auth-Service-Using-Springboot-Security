package com.auth.customauthservice.dtos;

import com.auth.customauthservice.models.Role;
import com.auth.customauthservice.models.User;
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

    public static UserResponseDto from(User user){
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setRoles(user.getRoles());
        return userResponseDto;
    }

}
