package com.tenPines.application.service.validator.rule;

import com.tenPines.model.User;

public class NotTheSamePersonRule implements ValidFriendRelationRule{
    @Override
    public boolean evaluate(User giver, User receiver) {
        return !giver.equals(receiver);
    }

    @Override
    public boolean softRule() {
        return false;
    }
}
