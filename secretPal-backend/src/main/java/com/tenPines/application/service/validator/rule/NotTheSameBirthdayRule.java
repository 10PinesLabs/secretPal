package com.tenPines.application.service.validator.rule;

import com.tenPines.model.User;

public class NotTheSameBirthdayRule implements ValidFriendRelationRule{

    @Override
    public boolean evaluate(User giver, User receiver) {
        return !giver.getWorker().getDateOfBirth().equals(receiver.getWorker().getDateOfBirth());
    }

    @Override
    public boolean softRule() {
        return true;
    }
}
