package com.oyo.backend.service;

import com.oyo.backend.model.User;

import java.util.List;

public interface IUserService {
    User registerUser( User user);

    List<User> getAllUsers();

    void deleteUser( String mail );

    User getUser( String mail );
}
