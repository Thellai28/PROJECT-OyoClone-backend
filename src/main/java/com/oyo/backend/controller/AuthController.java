package com.oyo.backend.controller;

import com.oyo.backend.exception.UserAlreadyExistsException;
import com.oyo.backend.model.User;
import com.oyo.backend.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@RequiredArgsConstructor
public class AuthController {

    private final IUserService userService;

    @PostMapping("/register-user" )
    public ResponseEntity<?> registerUser(User user ){
        try{
            userService.registerUser(user);
            return ResponseEntity.ok("Registation successful" );
        }catch(UserAlreadyExistsException e ){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());

        }
    }


}