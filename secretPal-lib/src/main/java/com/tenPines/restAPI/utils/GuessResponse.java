package com.tenPines.restAPI.utils;

import java.util.List;

public class GuessResponse {
    public boolean wasGuessed;
    public List<String> guessAttempts;

    public GuessResponse(boolean wasGuessed, List<String> guessAttempts) {
        this.wasGuessed = wasGuessed;
        this.guessAttempts = guessAttempts;
    }
}
