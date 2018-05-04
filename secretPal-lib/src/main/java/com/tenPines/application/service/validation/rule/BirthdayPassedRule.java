package com.tenPines.application.service.validation.rule;

import com.tenPines.application.clock.Clock;
import com.tenPines.model.FriendRelation;
import com.tenPines.model.Worker;

import java.time.LocalDate;
import java.util.List;

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
    public Boolean validate(FriendRelation relation, List<FriendRelation> newRelations) {
        LocalDate actualBirthday = relation.getGiftReceiver().getDateOfBirth().withYear(clock.now().getYear());
        return clock.now().isBefore(actualBirthday);
    }

    @Override
    public boolean isActive() {
        return true;
    }

}
