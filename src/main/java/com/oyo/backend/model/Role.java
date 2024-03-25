package com.oyo.backend.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.apache.bcel.classfile.Module;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;

    @ManyToMany( mappedBy = "roles") // "roles" collections form the user entity or class :
    private Collection<User> users  = new HashSet<>();

    public Role( String roleName ) {
        this.name = roleName;
    }

    public void assignRoleToUser(User user ){
        user.getRoles().add(this);
        this.getUsers().add(user);
    }

    public void removeUserFromRoles(User user ){
        user.getRoles().remove(this);
        this.getUsers().remove(user);
    }

    public void removeAllUsersFromRole(){
        if( this.getUsers() != null ){
            List<User> roleUsers = this.getUsers().stream().toList();
            roleUsers.forEach( this :: removeUserFromRoles );
        }
    }

    public String getName (){
        return (name != null ) ? name : "";
    }
}
