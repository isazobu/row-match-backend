package com.isazobu.rowmatch.backend.exceptions;

public class NotEnoughCoinsException extends Throwable {
    public NotEnoughCoinsException(String notEnoughCoins) {
        super(notEnoughCoins);
    }
}
