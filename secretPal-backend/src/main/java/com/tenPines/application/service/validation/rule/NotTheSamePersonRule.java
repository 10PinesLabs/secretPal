package com.tenPines.application.service.validation.rule;

import com.tenPines.model.Worker;

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

}
