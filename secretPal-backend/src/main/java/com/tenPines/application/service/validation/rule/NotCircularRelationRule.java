package com.tenPines.application.service.validation.rule;

import com.tenPines.application.service.FriendRelationService;
import com.tenPines.model.FriendRelation;
import com.tenPines.model.Worker;

public class NotCircularRelationRule extends AssignationRule {

    private FriendRelationService friendRelationService;

    public NotCircularRelationRule(FriendRelationService friendRelationService) {
        this.friendRelationService = friendRelationService;
    }

    @Override
    public Boolean validate(Worker giver, Worker receiver) {
        FriendRelation workerRelation = friendRelationService.getByWorkerReceiver(giver);
        return (workerRelation == null) || !(workerRelation.getGiftGiver().equals((receiver)));
    }
}
