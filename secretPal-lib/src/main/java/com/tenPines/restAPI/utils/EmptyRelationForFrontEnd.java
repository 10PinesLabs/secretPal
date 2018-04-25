package com.tenPines.restAPI.utils;

import com.tenPines.model.Hint;

import java.util.ArrayList;
import java.util.List;

public class EmptyRelationForFrontEnd extends com.tenPines.model.FriendRelation {

    @Override
    public boolean isGuessed() {
        return false;
    }

    @Override
    public List<String> getGuessAttempts() {
        return new ArrayList<>();
    }

    @Override
    public List<Hint> hints() {
        return new ArrayList<>();
    }
}
