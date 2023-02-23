package com.isazobu.rowmatch.backend.exceptions;

public class TeamJoinNotAllowedException extends RuntimeException {
    public TeamJoinNotAllowedException(String message) {
        super(message);
    }
}
