package com.auth.customauthservice.services;

import com.auth.customauthservice.dtos.UserResponseDto;
import com.auth.customauthservice.exceptions.UserAlreadyExistsException;
import com.auth.customauthservice.models.Session;
import com.auth.customauthservice.models.SessionStatus;
import com.auth.customauthservice.models.User;
import com.auth.customauthservice.repositories.SessionRepository;
import com.auth.customauthservice.repositories.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMapAdapter;

import java.util.*;

@Service
public class AuthService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCrypt;
    private SessionRepository sessionRepository;

    @Autowired
    public AuthService(UserRepository userRepository, BCryptPasswordEncoder bCrypt, SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.bCrypt = bCrypt;
        this.sessionRepository = sessionRepository;
    }

    public ResponseEntity<UserResponseDto> login(String email, String password){
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            throw new RuntimeException();
        }

        if(!bCrypt.matches(password, optionalUser.get().getPassword())){
            throw new RuntimeException();
        }

        String token = RandomStringUtils.randomAscii(20);

        Session session = new Session();
        session.setSessionStatus(SessionStatus.ACTIVE);
        session.setUser(optionalUser.get());
        session.setToken(token);
        sessionRepository.save(session);

        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setEmail(optionalUser.get().getEmail());
        MultiValueMapAdapter<String, String> headers = new MultiValueMapAdapter<>(new HashMap<>());
        headers.add("AUTH_TOKEN", token);

        return new ResponseEntity<UserResponseDto>(
                userResponseDto,
                headers,
                HttpStatus.OK
        );
    }
    public UserResponseDto signUp(String email, String password) throws UserAlreadyExistsException {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (!optionalUser.isEmpty()) {
            throw new UserAlreadyExistsException("User with email: " + email + " is already present.");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(bCrypt.encode(password));
        userRepository.save(user);
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setEmail(email);
        return userResponseDto;
    }

    public SessionStatus validateToken(String token, Long userId) {
        Optional<Session> optionalSession = sessionRepository.findByTokenAndUser_Id(token, userId);

        if (optionalSession.isEmpty()) {
            return SessionStatus.INVALID;
        }

        Session session = optionalSession.get();

//        if(!session.getExpirtyTime() > new Date()){
//            return SessionStatus.EXPIRED;
//        }

        if(!SessionStatus.ACTIVE.equals(session.getSessionStatus())){
            return SessionStatus.EXPIRED;
        }
        return SessionStatus.ACTIVE;
    }

    public ResponseEntity<Void> logout(String token, Long userId){
        Optional<Session> optionalSession = sessionRepository.findByTokenAndUser_Id(token, userId);

        if(optionalSession.isEmpty()){
            return null;
        }
        Session session = optionalSession.get();

        session.setSessionStatus(SessionStatus.LOGGED_OUT);

        sessionRepository.save(session);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
