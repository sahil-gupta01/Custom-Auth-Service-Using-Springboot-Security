package com.auth.customauthservice.controllers;

import com.auth.customauthservice.dtos.LoginRequestDto;
import com.auth.customauthservice.dtos.UserResponseDto;
import com.auth.customauthservice.dtos.ValidateTokenRequestDto;
import com.auth.customauthservice.exceptions.UserAlreadyExistsException;
import com.auth.customauthservice.models.SessionStatus;
import com.auth.customauthservice.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDto> login(@RequestBody LoginRequestDto requestDto){
        return authService.login(requestDto.getEmail(), requestDto.getPassword());
    }
    @PostMapping("/signUp")
    public ResponseEntity<UserResponseDto> signUp(@RequestBody LoginRequestDto requestDto) throws UserAlreadyExistsException {
        UserResponseDto userResponse = authService.signUp(requestDto.getEmail(), requestDto.getPassword());
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }
    @PostMapping("/validate")
    public ResponseEntity<SessionStatus> validateToken(@RequestBody ValidateTokenRequestDto validateTokenRequestDto){
        SessionStatus sessionStatus = authService.validateToken(validateTokenRequestDto.getToken(), validateTokenRequestDto.getUserId());
        return new ResponseEntity<>(sessionStatus, HttpStatus.OK);
    }
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody ValidateTokenRequestDto validateTokenRequestDto){
        return authService.logout(validateTokenRequestDto.getToken(), validateTokenRequestDto.getUserId());
    }
}
