package com.tenPines.application.service.validation.rule;

import com.tenPines.application.service.FriendRelationService;
import com.tenPines.model.Worker;
import org.springframework.stereotype.Component;

@Component
public class RuleValidator {

    public static FriendRelationService friendRelationService;

    public RuleValidator(FriendRelationService friendRelationService) {
        this.friendRelationService = friendRelationService;
    }

    public static Boolean validate(Worker giver, Worker receiver) {
        return friendRelationService.getByWorkerReceiver(giver)
                .map(relation -> !relation.getGiftGiver().equals(receiver))
                .orElse(true);
    }
}
