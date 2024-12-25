//package com.experience.Xperience.controllers;
//import com.experience.Xperience.models.User;
//import com.experience.Xperience.services.JwtService;
//import com.experience.Xperience.services.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//import javax.management.remote.JMXAuthenticator;
//
//@Controller
//public class UserController {
//    @Autowired
//    private UserService userService;
//
//
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private JwtService jwtService;
//
//    AuthenticationManager authenticationManager;
//
//    @PostMapping("auth/register")
//    public ResponseEntity<?> register(@RequestBody User user) {
//        if (user.getPassword() == null || user.getPassword().isEmpty()) {
//            return ResponseEntity.badRequest().body("Password cannot be null or empty");
//        }
//        User user1 = userService.getUserByUsername(user.getUsername());
//        if (user1 != null) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists with given username..");
//        }
//        userService.saveUser(user);
//        return ResponseEntity.ok("User registered successfully");
//    }
//
//    @PostMapping("auth/login")
//    public ResponseEntity<?> login(@RequestBody User user) {
//        Authentication authentication = authenticationManager
//                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
//
//        if(authentication.isAuthenticated()) {
//            String token = jwtService.generateToken(user.getUsername());
//            return ResponseEntity.status(HttpStatus.OK).body(token);
//        }
//        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid username or passoword..");
//    }
//}
package com.experience.Xperience.controllers;

import com.experience.Xperience.models.User;
import com.experience.Xperience.services.JwtService;
import com.experience.Xperience.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder; // Autowired to inject PasswordEncoder

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager; // Autowired to inject AuthenticationManager

    @PostMapping("auth/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return ResponseEntity.badRequest().body("Password cannot be null or empty");
        }

        // Encrypting the password before saving
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword); // Set the encoded password

        User existingUser = userService.getUserByUsername(user.getUsername());
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists with given username.");
        }

        userService.saveUser(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("auth/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(user.getUsername());
            return ResponseEntity.status(HttpStatus.OK).body(token);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid username or password.");
    }

    @GetMapping("user/profile/{username}")
    public ResponseEntity<?> userProfile(@PathVariable String username) {
        User user=userService.getUserByUsername(username);
        if(user==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("no user profile exists..");
        }
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    //@PutMapping("user/profile/{username}")
}

