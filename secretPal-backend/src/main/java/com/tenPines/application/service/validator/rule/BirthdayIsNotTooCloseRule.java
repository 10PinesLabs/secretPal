package com.tenPines.application.service.validator.rule;

import com.tenPines.model.User;

import java.time.LocalDate;

public class BirthdayIsNotTooCloseRule implements ValidFriendRelationRule {
    @Override
    public boolean evaluate(User giver, User receiver) {
        LocalDate receiverDateOfBirth = receiver.getWorker().getDateOfBirth();
        LocalDate thisDayOnTheReceiversYear = LocalDate.now().withYear(receiverDateOfBirth.getYear());
        return receiverDateOfBirth.plusWeeks(-2).isAfter(thisDayOnTheReceiversYear);
    }

    @Override
    public boolean softRule() {
        return false;
    }
}
