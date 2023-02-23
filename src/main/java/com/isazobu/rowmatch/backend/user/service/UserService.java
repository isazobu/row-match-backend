package com.isazobu.rowmatch.backend.user.service;

import com.isazobu.rowmatch.backend.user.dto.CreateUserRequest;
import com.isazobu.rowmatch.backend.user.model.User;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.List;

public interface UserService {
    User createUser(CreateUserRequest request);

    User updateLevel(User user);


    String createHashWithSalt(final String textToHash) throws NoSuchAlgorithmException;

    User verifyToken(String token);

    byte[] createSalt() throws NoSuchAlgorithmException,
            NoSuchProviderException;

    String convertToHex(byte[] data);

    List<User> initUsers();

}