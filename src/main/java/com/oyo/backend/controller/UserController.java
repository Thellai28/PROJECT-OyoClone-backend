package com.oyo.backend.controller;

import com.oyo.backend.model.User;
import com.oyo.backend.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.rmi.server.ExportException;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<User>> getUsers(){
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.FOUND);
    }

    @GetMapping("/{email")
    public ResponseEntity<?> getUserByEmail( @PathVariable ("email") String email ){
        try{
            User theUser = userService.getUser(email);
            return ResponseEntity.ok(theUser);
        }catch( UsernameNotFoundException ex ){
            return ResponseEntity.status( HttpStatus.NOT_FOUND).body(ex.getMessage() );
        }catch( Exception ex ){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error Fetching User" );
        }
    }

    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") String email ){
        try{
            userService.deleteUser(email);
            return ResponseEntity.ok("User deleted successfully" );

        }catch (UsernameNotFoundException e ){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch(Exception e ){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body( "Error deleting user" );
        }
    }



}
