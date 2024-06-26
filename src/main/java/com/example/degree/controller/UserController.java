package com.example.degree.controller;

import com.example.degree.entities.AuthRequest;
import com.example.degree.entities.LoginResponse;
import com.example.degree.entities.Users;
import com.example.degree.repositories.UsersRepo;
import com.example.degree.service.JwtService;
import com.example.degree.service.UserService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    UsersRepo usersRepo;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtService jwtService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping
    public ResponseEntity<?> addUser(@RequestBody Users userInfo) {

        Optional<Users> existingUser = usersRepo.findByUserName(userInfo.getUserName());
        Optional<Users> existingUserEmail = usersRepo.findByEmail(userInfo.getEmail());
        if (existingUser.isPresent() && existingUserEmail.isPresent()) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("error", "Username and Email already exist");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);
        } else if (existingUser.isPresent()) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("error", "Username already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);
        } else if (existingUserEmail.isPresent()) {
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("error", "Email already exists");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseBody);
        }


        userService.saveUser(userInfo);
         String token = jwtService.generateToken(userInfo.getUserName());
          Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", userInfo);

        return ResponseEntity.ok(response);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping
    public ResponseEntity<List<Users>> getAllUsers() {
        List<Users> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/{id}")
    public ResponseEntity<Users> getUserById(@PathVariable Long id) {
        Users user = userService.getUserById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));

            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(authRequest.getUsername());
                Optional<Users> optionalUser = usersRepo.findByUserName(authRequest.getUsername());

                if (optionalUser.isPresent()) {
                    Users user = optionalUser.get();
                    LoginResponse response = new LoginResponse(token, user);
                    return ResponseEntity.ok(response);
                } else {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
                }
            } else {
                throw new UsernameNotFoundException("Invalid user request");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
}
