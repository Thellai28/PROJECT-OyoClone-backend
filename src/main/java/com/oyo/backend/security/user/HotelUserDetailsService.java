package com.oyo.backend.security.user;

import com.oyo.backend.model.User;
import com.oyo.backend.repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HotelUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    /*
       UserDetails is not an entity, it just a template, to hold the user details, UserDetails is an interface, so
       we have to provide implementation for it, for that purpose, we use HotelUserDetails in this project.
       Generally we use service to hold the logic related to that particular entity, Here the userDetails is not
       an entity, so we don't have UserDetailsRepository, instead we use the UserRepository to fetch the userDetails.

    */

    @Override
    public UserDetails loadUserByUsername( String email ) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
        return HotelUserDetails.buildUserDetails(user);
    }
}
