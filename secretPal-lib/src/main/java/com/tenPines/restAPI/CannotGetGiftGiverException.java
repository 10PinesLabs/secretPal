package com.tenPines.restAPI;

public class CannotGetGiftGiverException extends RuntimeException {
    public CannotGetGiftGiverException() {
        super("The gift giver cannot be returned until it has been guessed.");
    }
}
