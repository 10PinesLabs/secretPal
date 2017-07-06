package com.tenPines.application.service.validation.rule;

import com.tenPines.application.service.FriendRelationService;
import com.tenPines.model.FriendRelation;
import com.tenPines.model.Worker;

public class NotCircularRelationRule extends AssignationRule {

    private FriendRelationService friendRelationService;
    public String description;
    public Boolean isActivate;

    public NotCircularRelationRule(FriendRelationService friendRelationService) {
        this.friendRelationService = friendRelationService;
        this.description = "Si el Pino A le regala a el Pino B, el Pino B no le puede regalar a el Pino A.";
        this.isActivate = false;
    }

    @Override
    public Boolean validate(Worker giver, Worker receiver) {
        FriendRelation workerRelation = friendRelationService.getByWorkerReceiver(giver);
        return (workerRelation == null) || !(workerRelation.getGiftGiver().equals((receiver)));
    }

    public void changeRuleIntention() {
        setIsActive(!isActivate);
    }

    public boolean isRuleActivate() {
        return this.isActivate;
    }

    public void setIsActive(Boolean newState) {
        this.isActivate = newState;
    }
}
