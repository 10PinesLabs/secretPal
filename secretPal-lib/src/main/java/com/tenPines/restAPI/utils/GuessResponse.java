package com.tenPines.restAPI.utils;

public class GuessResponse {
    public boolean wasGuessed;
    public int guessAttempts;

    public GuessResponse(boolean wasGuessed, int guessAttempts) {
        this.wasGuessed = wasGuessed;
        this.guessAttempts = guessAttempts;
    }
}
