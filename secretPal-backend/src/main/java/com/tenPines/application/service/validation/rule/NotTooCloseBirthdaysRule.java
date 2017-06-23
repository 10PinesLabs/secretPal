package com.tenPines.application.service.validation.rule;

import com.tenPines.model.Worker;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.temporal.ChronoUnit;

public class NotTooCloseBirthdaysRule extends AssignationRule {

    @Override
    public Boolean validate(Worker giver, Worker receiver) {
        return moreThan2WeeksBetweenBirthdays(giver, receiver);
    }

    private Boolean moreThan2WeeksBetweenBirthdays(Worker giver, Worker receiver) {
        return weeksBetweenBirthdays(giver, receiver) >= 2;
    }

    private Long weeksBetweenBirthdays(Worker giver, Worker receiver) {
        return Math.abs(ChronoUnit.WEEKS.between(actualWorkerBirthday(giver), actualWorkerBirthday(receiver)));
    }

    private LocalDate actualWorkerBirthday(Worker giver) {
        return MonthDay.from(giver.getDateOfBirth()).atYear(LocalDate.now().getYear());
    }

}
