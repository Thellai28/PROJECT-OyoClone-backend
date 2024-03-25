package com.oyo.backend.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.boot.model.source.spi.CollectionIdSource;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    private String password;

    @ManyToMany(
            fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH})
    @JoinTable( name = "User_roles",
            joinColumns = @JoinColumn(name = "User_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn( name = "role_id", referencedColumnName = "id") )
    private Collection<Role> roles = new HashSet<>();

}
