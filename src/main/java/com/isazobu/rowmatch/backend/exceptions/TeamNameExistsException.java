package com.isazobu.rowmatch.backend.exceptions;

public class TeamNameExistsException extends RuntimeException {
    public TeamNameExistsException(String message) {
        super(message);
    }
}
