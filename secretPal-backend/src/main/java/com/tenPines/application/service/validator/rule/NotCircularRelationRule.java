package com.tenPines.application.service.validator.rule;

import com.tenPines.application.service.FriendRelationService;
import com.tenPines.model.User;

public class NotCircularRelationRule implements ValidFriendRelationRule {
    public FriendRelationService friendRelationService;

    public NotCircularRelationRule(FriendRelationService friendRelationSv){
       friendRelationService = friendRelationSv;
    }

    @Override
    public boolean evaluate(User giver, User receiver) {
        return friendRelationService.getByWorkerReceiver(giver.getWorker()) == null || !friendRelationService.getByWorkerReceiver(giver.getWorker()).getGiftGiver().equals(receiver.getWorker());
    }

    @Override
    public boolean softRule() {
        return true;
    }
}
