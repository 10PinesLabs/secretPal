package com.tenPines.application.service.validation.rule;

import com.tenPines.model.Worker;

import java.time.temporal.ChronoUnit;

public class NotTooCloseBirthdaysRule extends AssignationRule {

    @Override
    public Boolean validate(Worker giver, Worker receiver) {
        return ChronoUnit.WEEKS.between(giver.getDateOfBirth(), receiver.getDateOfBirth()) > 2;
    }

}
