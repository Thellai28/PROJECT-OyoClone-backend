package com.oyo.backend.controller;

import com.oyo.backend.exception.UserAlreadyExistsException;
import com.oyo.backend.model.User;
import com.oyo.backend.request.LoginRequest;
import com.oyo.backend.response.JwtResponse;
import com.oyo.backend.security.jwt.JwtUtils;
import com.oyo.backend.security.user.HotelUserDetails;
import com.oyo.backend.service.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/")
public class AuthController {

    private final IUserService userService;

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    @PostMapping("/register-user" )
    public ResponseEntity<?> registerUser(User user ){
        try{
            userService.registerUser(user);
            return ResponseEntity.ok("Registation successful" );
        }catch(UserAlreadyExistsException e ){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());

        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser( @Valid @RequestBody LoginRequest request ){
        Authentication authentication =
                authenticationManager
                        .authenticate( new UsernamePasswordAuthenticationToken( request.getEmail(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtTokenForUser(authentication);
        HotelUserDetails userDetails = (HotelUserDetails) authentication.getPrincipal();
        List<String> roles =  userDetails
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return ResponseEntity.ok( new JwtResponse(
                userDetails.getId(),
                userDetails.getEmial(),
                jwt,
                roles
        ));

    }


}