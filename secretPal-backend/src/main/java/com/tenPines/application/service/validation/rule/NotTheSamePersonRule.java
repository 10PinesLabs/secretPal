package com.tenPines.application.service.validation.rule;

import com.tenPines.model.FriendRelation;
import com.tenPines.model.Worker;

import java.util.List;

public class NotTheSamePersonRule extends AssignationRule {

    @Override
    public Boolean validate(Worker giver, Worker receiver) {
        return !giver.equals(receiver);
    }

    @Override
    public Boolean validate(FriendRelation relation, List<FriendRelation> newRelations) {
        return validate(relation.getGiftGiver(), relation.getGiftReceiver());
    }

}
