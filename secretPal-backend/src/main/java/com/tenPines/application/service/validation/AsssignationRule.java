package com.tenPines.application.service.validation;

import com.tenPines.model.User;
import com.tenPines.model.Worker;

public abstract class AsssignationRule {

    public abstract Boolean validate(Worker worker, Worker otherWorker);

    public abstract boolean softRule();

    public abstract boolean evaluate(User giver, User receiver);
}
