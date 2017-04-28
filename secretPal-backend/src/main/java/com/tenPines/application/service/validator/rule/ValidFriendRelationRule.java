package com.tenPines.application.service.validator.rule;

import com.tenPines.model.User;

public interface ValidFriendRelationRule {
    public boolean evaluate(User giver, User receiver);

    public boolean softRule();
}
