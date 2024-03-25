package com.oyo.backend.service;

import com.oyo.backend.exception.RoleAlreadyExistsException;
import com.oyo.backend.exception.UserAlreadyExistsException;
import com.oyo.backend.model.Role;
import com.oyo.backend.model.User;
import com.oyo.backend.repository.RoleRepository;
import com.oyo.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService{
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;


    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll() ;
    }

    @Override
    public Role createRole( Role theRole ) {
        String roleName = "ROLE_"+ theRole.getName().toUpperCase();
        Role role = new Role( roleName );

        if( roleRepository.existsByName(role)){
            throw new RoleAlreadyExistsException( role.getName() + " role already exists");
        }
        return roleRepository.save(role);
    }

    @Override
    public void deleteRole( Long roleId ) {
        this.removeAllUsersFromRole(roleId);




    }

    @Override
    public Role findByName( String name ) {
        return roleRepository.findByName(name).get();
    }

    @Override
    public User removeUserFromRole( Long userId, Long roleId ) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Role> role = roleRepository.findById(roleId);

        if( role.isPresent() && role.get().getUsers().contains(user.get())){
            role.get().removeUserFromRoles(user.get()); // Each and every role has its own hashSet, in that case,
            // we have to specify some methods inside the the Role entity itself :
            roleRepository.save( role.get() );
            return user.get();
        }

        throw new UsernameNotFoundException("User Not Found");
    }

    @Override
    public User assignRoleToUser( Long userId, Long roleId ) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Role> role = roleRepository.findById(roleId);

        if( user.isPresent() && user.get().getRoles().contains(role.get())){
            throw new UserAlreadyExistsException(user.get().getFirstName() + " is already assigned to the " +
                            role.get().getName() + " role");
        }

        if( role.isPresent()){
            role.get().assignRoleToUser(user.get());
            roleRepository.save( role.get());
        }
        return user.get();
    }

    @Override
    public Role removeAllUsersFromRole( Long roleId ) {
        Optional<Role> role = roleRepository.findById(roleId);
        role.get().removeAllUsersFromRole();
        return roleRepository.save(role.get());
    }
}
