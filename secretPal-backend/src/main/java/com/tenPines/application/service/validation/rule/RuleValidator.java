package com.tenPines.application.service.validation.rule;

import com.tenPines.application.service.FriendRelationService;
import com.tenPines.model.FriendRelation;
import com.tenPines.model.Worker;
import org.springframework.stereotype.Component;

@Component
public class RuleValidator {

    public static FriendRelationService friendRelationService;

    public RuleValidator(FriendRelationService friendRelationService) {
        this.friendRelationService = friendRelationService;
    }

    public static Boolean validate(Worker giver, Worker receiver) {
        FriendRelation workerRelation = friendRelationService.getByWorkerReceiver(giver);
        return (workerRelation == null) || !(workerRelation.getGiftGiver().equals((receiver)));
    }
}
