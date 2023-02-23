package com.isazobu.rowmatch.backend.exceptions;

public class TeamCapacityFullException extends RuntimeException {
    public TeamCapacityFullException(String message) {
        super(message);
    }
}
