package com.tenPines.model;


import com.tenPines.application.service.FriendRelationService;
import com.tenPines.application.service.WorkerService;
import com.tenPines.application.service.validation.rule.AssignationRule;
import com.tenPines.application.service.validation.rule.NotCircularRelationRule;
import com.tenPines.application.service.validation.rule.NotTheSamePersonRule;
import com.tenPines.application.service.validation.rule.NotTooCloseBirthdaysRule;
import com.tenPines.builder.WorkerBuilder;
import com.tenPines.integration.SpringBaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AssignationRuleTest extends SpringBaseTest {

    @Autowired
    public FriendRelationService friendRelationService;
    @Autowired
    public WorkerService workerService;

    @Test
    public void when_participant_A_gifts_participant_B_then_this_cannot_gifts_participant_A() {
        Worker giver = new WorkerBuilder().build();
        Worker receiver = new WorkerBuilder().build();
        workerService.save(giver);
        workerService.save(receiver);

        friendRelationService.create(giver, receiver);
        AssignationRule rule = new NotCircularRelationRule();

        assertFalse(rule.validate(receiver, giver));
    }

    @Test
    public void a_participant_cannot_gifts_himself() {
        Worker worker = new WorkerBuilder().build();
        workerService.save(worker);

        AssignationRule rule = new NotTheSamePersonRule();

        assertFalse(rule.validate(worker, worker));
    }

    @Test
    public void when_participants_have_same_birthday_date_then_isnt_valid_relation() {
        Worker worker = new WorkerBuilder().build();
        Worker otherWorker = new WorkerBuilder().build();
        worker.setDateOfBirth(LocalDate.of(2000, 5, 1));
        otherWorker.setDateOfBirth(LocalDate.of(1990, 5, 1));
        workerService.save(worker);
        workerService.save(otherWorker);

        AssignationRule rule = new NotTooCloseBirthdaysRule();

        assertFalse(rule.validate(worker, otherWorker));
    }

    @Test
    public void participants_have_less_than_2_weeks_between_birthday_dates_then_isnt_valid_relation() {
        Worker worker = new WorkerBuilder().build();
        Worker otherWorker = new WorkerBuilder().build();
        worker.setDateOfBirth(LocalDate.of(2000, 5, 1));
        otherWorker.setDateOfBirth(LocalDate.of(1990, 5, 8));
        workerService.save(worker);
        workerService.save(otherWorker);

        AssignationRule rule = new NotTooCloseBirthdaysRule();

        assertFalse(rule.validate(worker, otherWorker));
        assertFalse(rule.validate(otherWorker, worker));
    }

    @Test
    public void participants_have_more_than_2_weeks_between_birthday_dates_then_is_valid_relation() {
        Worker worker = new WorkerBuilder().build();
        Worker otherWorker = new WorkerBuilder().build();
        worker.setDateOfBirth(LocalDate.of(2000, 5, 1));
        otherWorker.setDateOfBirth(LocalDate.of(1990, 5, 16));
        workerService.save(worker);
        workerService.save(otherWorker);

        AssignationRule rule = new NotTooCloseBirthdaysRule();

        assertTrue(rule.validate(worker, otherWorker));
        assertTrue(rule.validate(otherWorker, worker));
    }

}
