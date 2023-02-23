package com.isazobu.rowmatch.backend.user.service;


import com.isazobu.rowmatch.backend.exceptions.EntityAlreadyExistsException;
import com.isazobu.rowmatch.backend.user.dto.CreateUserRequest;
import com.isazobu.rowmatch.backend.user.dto.UpdateLevelRequest;
import com.isazobu.rowmatch.backend.user.model.User;
import com.isazobu.rowmatch.backend.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(CreateUserRequest request) {
        Optional<User> userOptional = userRepository.findByName(request.getName());
        if (userOptional.isPresent()) {
            throw new EntityAlreadyExistsException("User name already exists");
        }

        User user = new User();
        user.setName(request.getName());
        try {
            user.setToken(this.createHashWithSalt(request.getName()));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        userRepository.save(user);
        return user;
    }

    @Override
    public UpdateLevelRequest updateLevel(String token) throws EntityNotFoundException {
        User user = verifyToken(token);

        if (user == null) {
            throw new EntityNotFoundException("User not found");
        }
        user.setLevel(user.getLevel() + 1);
        user.setCoins(user.getCoins() + 25);
        userRepository.save(user);
        // user response without token
        UpdateLevelRequest updateLevelRequest = new UpdateLevelRequest();
        updateLevelRequest.setId(user.getId());
        updateLevelRequest.setName(user.getName());
        updateLevelRequest.setLevel(user.getLevel());
        updateLevelRequest.setCoins(user.getCoins());
        return updateLevelRequest;


    }


    @Override
    public String createHashWithSalt(final String textToHash) throws NoSuchAlgorithmException {
        try {
            byte[] salt = createSalt();

            String saltedHash = null;
            // Create MessageDigest instance for Sha-256
            MessageDigest md = MessageDigest.getInstance("Sha-256");

            //Add salted bytes to digest
            md.update(salt);

            //Get the hash's bytes
            byte[] bytes = md.digest(textToHash.getBytes());

            saltedHash = convertToHex(bytes);
            return saltedHash;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public String convertToHex(final byte[] messageDigest) {
        BigInteger bigint = new BigInteger(1, messageDigest);
        String hexText = bigint.toString(16);
        while (hexText.length() < 32) {
            hexText = "0".concat(hexText);
        }
        return hexText;
    }

    //Create salt
    public byte[] createSalt()
            throws NoSuchAlgorithmException,
            NoSuchProviderException {

        //Always use a SecureRandom generator for random salt
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "SUN");
        //Create array for salt
        byte[] salt = new byte[16];
        //Get a random salt
        sr.nextBytes(salt);
        //return salt
        return salt;
    }


    @Override
    public User verifyToken(String token) {
        User user = userRepository.findByToken(token)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return user;
    }

    @Override
    public List<User> initUsers() {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setName("user" + i);
            user.setLevel(1);
            user.setCoins(5000);
            user.setToken(UUID.randomUUID().toString());

            users.add(user);
        }


        userRepository.saveAll(users);
        return users;
    }

}






