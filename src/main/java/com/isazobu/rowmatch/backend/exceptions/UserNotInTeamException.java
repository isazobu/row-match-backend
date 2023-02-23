package com.isazobu.rowmatch.backend.exceptions;

public class UserNotInTeamException extends RuntimeException {
    public UserNotInTeamException(String message) {
        super(message);
    }
}
