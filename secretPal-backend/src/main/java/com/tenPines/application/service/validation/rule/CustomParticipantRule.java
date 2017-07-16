package com.tenPines.application.service.validation.rule;

import com.tenPines.model.FriendRelation;
import com.tenPines.application.service.CustomParticipantRuleService;
import com.tenPines.model.Worker;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.List;

@Entity
@Table
public class CustomParticipantRule extends AssignationRule {

    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    public Worker giver;
    @OneToOne
    public Worker receiver;
    @NotNull
    private Boolean isActive;

    public CustomParticipantRule() {
    }

    public CustomParticipantRule(Worker giver, Worker receiver, Boolean isActive) {
        this.giver = giver;
        this.receiver = receiver;
        this.isActive = isActive;
    }

    @Override
    public Boolean validate(Worker giver, Worker receiver) {
        return this.giver.equals(giver) && this.receiver.equals(receiver);
    }

    @Override
    public Boolean validate(FriendRelation relation, List<FriendRelation> newRelations) {
        return validate(relation.getGiftGiver(), relation.getGiftReceiver());
    }

    public void changeRuleIntention() {
        setIsActive(!isActive);
    }

    public boolean isActive() {
        return this.isActive;
    }

    public void setIsActive(Boolean newState) {
        this.isActive = newState;
    }

    public Long getId() {
        return id;
    }
}
