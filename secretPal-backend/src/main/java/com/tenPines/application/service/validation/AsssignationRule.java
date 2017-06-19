package com.tenPines.application.service.validation;

import com.tenPines.model.Worker;

public abstract class AsssignationRule {

    public abstract Boolean validate(Worker worker, Worker otherWorker);
}
