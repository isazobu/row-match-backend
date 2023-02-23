package com.isazobu.rowmatch.backend.user.controller;

import com.isazobu.rowmatch.backend.user.dto.CreateUserRequest;
import com.isazobu.rowmatch.backend.user.dto.UpdateLevelRequest;
import com.isazobu.rowmatch.backend.user.model.User;
import com.isazobu.rowmatch.backend.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<UpdateLevelRequest> updateLevel(@RequestHeader("Authorization") String token) {
        if (token == null || token.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        UpdateLevelRequest user = userService.updateLevel(token);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }



}

