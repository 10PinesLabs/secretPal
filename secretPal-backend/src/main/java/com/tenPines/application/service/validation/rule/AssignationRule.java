package com.tenPines.application.service.validation.rule;

import com.tenPines.model.User;
import com.tenPines.model.Worker;

public abstract class AssignationRule {

    public abstract Boolean validate(Worker giver, Worker receiver);

}