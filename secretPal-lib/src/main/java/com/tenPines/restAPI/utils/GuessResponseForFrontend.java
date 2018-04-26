package com.tenPines.restAPI.utils;

import com.tenPines.model.Worker;

import java.util.List;

public class GuessResponseForFrontend {
    public Worker secretPine;
    public boolean wasGuessed;
    public List<String> guessAttempts;

    public GuessResponseForFrontend(boolean wasGuessed, List<String> guessAttempts, Worker secretPine) {
        this.wasGuessed = wasGuessed;
        this.guessAttempts = guessAttempts;
        this.secretPine = secretPine;
    }
}
