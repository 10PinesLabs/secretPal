package com.tenPines.application.service.validation.rule;

import com.tenPines.application.service.FriendRelationService;
import com.tenPines.model.User;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class CustomParticipantRule {

    @Autowired
    public FriendRelationService friendRelationService;

    public boolean softRule() {
        return false;
    }

    //TODO: Seguir desde acá
    private boolean evaluate(User giver, User receiver) {
        return true;
    }
}
