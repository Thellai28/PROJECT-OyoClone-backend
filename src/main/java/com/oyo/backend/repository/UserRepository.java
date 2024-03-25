package com.oyo.backend.repository;

import com.oyo.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    void deleteByEmail( String mail );

    Optional<User> findByEmail( String mail );

    boolean existByEmail( String email );
}
