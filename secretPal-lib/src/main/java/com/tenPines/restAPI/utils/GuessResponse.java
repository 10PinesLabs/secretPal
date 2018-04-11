package com.tenPines.restAPI.utils;

public class GuessResponse {
    public boolean wasGuessed;
    public int remainingGuessAttempts;

    public GuessResponse(boolean wasGuessed, int remainingGuessAttempts) {
        this.wasGuessed = wasGuessed;
        this.remainingGuessAttempts = remainingGuessAttempts;
    }
}
