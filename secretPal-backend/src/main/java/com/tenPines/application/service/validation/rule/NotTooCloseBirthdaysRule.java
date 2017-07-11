package com.tenPines.application.service.validation.rule;

import com.tenPines.model.Worker;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.temporal.ChronoUnit;

@Entity
@Table
public class NotTooCloseBirthdaysRule extends AssignationRule {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    public String description;
    @NotNull
    public boolean isActive;

    public NotTooCloseBirthdaysRule() {
        this.description = "Esta permitido regalar aunque el cumpleaños del pino también esté cerca.";
    }

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

    public void changeRuleIntention() {
        setIsActive(!isActive);
    }

    public boolean isActive() {
        return this.isActive;
    }

    public void setIsActive(Boolean newState) {
        this.isActive = newState;
    }
}
