package com.tenPines.model;

import com.tenPines.application.clock.Clock;
import com.tenPines.application.service.validation.rule.AssignationRule;

import java.time.LocalDate;

public class BirthdayPassedRule extends AssignationRule {

    private final Clock clock;

    public BirthdayPassedRule(Clock clock) {
        this.clock = clock;
    }

    @Override
    public Boolean validate(Worker giver, Worker receiver) {
        LocalDate actualBirthday = receiver.getDateOfBirth().withYear(clock.now().getYear());
        return clock.now().isBefore(actualBirthday);
    }

    @Override
    public boolean isActive() {
        return true;
    }

}
