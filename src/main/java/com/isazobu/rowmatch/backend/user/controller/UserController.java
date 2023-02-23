package com.isazobu.rowmatch.backend.user.controller;

import com.isazobu.rowmatch.backend.user.dto.CreateUserRequest;
import com.isazobu.rowmatch.backend.user.model.User;
import com.isazobu.rowmatch.backend.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")

public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody CreateUserRequest request) {
        User user = userService.createUser(request);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }


    @PutMapping
    public ResponseEntity<User> updateLevel(@RequestHeader("Authorization") String token) {
        if (token == null || token.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        User user = userService.verifyToken(token);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        user = userService.updateLevel(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @PostMapping("/init")
    public ResponseEntity<List<User>> initUsers() {
        List<User> users = userService.initUsers();
        return new ResponseEntity<>(users, HttpStatus.CREATED);
    }

}
