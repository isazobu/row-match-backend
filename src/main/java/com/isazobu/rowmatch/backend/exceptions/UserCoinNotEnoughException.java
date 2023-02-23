package com.isazobu.rowmatch.backend.exceptions;

public class UserCoinNotEnoughException extends RuntimeException {
    public UserCoinNotEnoughException(String message) {
        super(message);
    }
}
