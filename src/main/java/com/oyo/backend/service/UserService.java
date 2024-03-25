package com.oyo.backend.service;

import com.oyo.backend.exception.UserAlreadyExistsException;
import com.oyo.backend.model.Role;
import com.oyo.backend.model.User;
import com.oyo.backend.repository.RoleRepository;
import com.oyo.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService implements IUserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;


    @Override
    public User registerUser( User user ) {
        if(userRepository.existByEmail(user.getEmail())){
            throw new UserAlreadyExistsException(user.getEmail() + " already exists" );
        }
        user.setPassword( passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName("ROLE_USER").get();
        user.setRoles(Collections.singletonList(userRole) );
        return userRepository.save(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    @Transactional
    @Override
    public void deleteUser( String mail ) {
        User theUser = getUser( mail );
        if( theUser != null ){
            userRepository.deleteByEmail( mail );
        }


    }

    @Override
    public User getUser( String mail ) {

        return userRepository.findByEmail( mail )
                .orElseThrow( ()-> new UsernameNotFoundException("User not found"));
    }
}
