package com.isazobu.rowmatch.backend.team.service;

public class NotEnoughCoinsException extends Throwable {
    public NotEnoughCoinsException(String notEnoughCoins) {
        super(notEnoughCoins);
    }
}
