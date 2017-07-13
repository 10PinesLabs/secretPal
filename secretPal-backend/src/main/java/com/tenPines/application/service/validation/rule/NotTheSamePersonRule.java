package com.tenPines.application.service.validation.rule;

import com.tenPines.model.FriendRelation;
import com.tenPines.model.Worker;

import java.util.List;


public class NotTheSamePersonRule extends AssignationRule {

    public Boolean isActive = true;

    @Override
    public Boolean validate(Worker giver, Worker receiver) {
        return !giver.equals(receiver);
    }

    @Override
    public boolean isActive( ) {
        return isActive;
    }

    @Override
    public Boolean validate(FriendRelation relation, List<FriendRelation> newRelations) {
        return validate(relation.getGiftGiver(), relation.getGiftReceiver());
    }

}
