package com.tenPines.application.service.validation.rule;

import com.tenPines.application.service.FriendRelationService;
import com.tenPines.model.FriendRelation;
import com.tenPines.model.Worker;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table
public class NotCircularRelationRule extends AssignationRule {

    @Id
    @GeneratedValue
    private Long id;

    @Transient
    private FriendRelationService friendRelationService;
    @NotNull
    public String description;
    @NotNull
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
