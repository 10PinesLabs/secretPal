package com.tenPines.application.service.validation.rule;

import com.tenPines.model.FriendRelation;
import com.tenPines.model.User;
import com.tenPines.model.Worker;

import java.util.List;

public abstract class AssignationRule {

    public abstract Boolean validate(Worker giver, Worker receiver);

    public abstract Boolean validate(FriendRelation relation, List<FriendRelation> newRelations);

}
