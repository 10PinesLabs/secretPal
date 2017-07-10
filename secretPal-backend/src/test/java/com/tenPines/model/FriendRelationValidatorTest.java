package com.tenPines.model;

import com.tenPines.application.clock.Clock;
import com.tenPines.application.service.FriendRelationService;
import com.tenPines.application.service.WorkerService;
import com.tenPines.application.service.validation.FriendRelationValidator;
import com.tenPines.builder.WorkerBuilder;
import com.tenPines.integration.SpringBaseTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FriendRelationValidatorTest extends SpringBaseTest {

    public FriendRelationValidator validator;
    @Autowired
    public Clock clock;
    @Autowired
    public FriendRelationService friendRelationService;
    @Autowired
    public WorkerService workerService;

    @Before
    public void setUp(){
        validator = new FriendRelationValidator(clock, friendRelationService);
    }

    @Test
    public void whenTheReceiverWillGiftTheGiverItShouldNotBeValid() {
        Worker giver = new WorkerBuilder().build();
        Worker receiver = new WorkerBuilder().build();
        workerService.save(giver);
        workerService.save(receiver);

        friendRelationService.create(giver, receiver);

        assertFalse(validator.validate(receiver, giver));
    }

    @Test
    public void whenTheReceiverIsTheSamePersonThanGiverItShouldNotBeValid() {
        Worker worker = new WorkerBuilder().build();
        workerService.save(worker);

        assertFalse(validator.validate(worker, worker));
    }

    @Test
    public void isInvalidWhenValidateTooCloseBirthdayRule() {
        Worker worker = new WorkerBuilder().build();
        Worker otherWorker = new WorkerBuilder().build();
        worker.setDateOfBirth(LocalDate.of(2000, 5, 1));
        otherWorker.setDateOfBirth(LocalDate.of(1990, 5, 8));
        workerService.save(worker);
        workerService.save(otherWorker);

        assertFalse(validator.validate(worker, otherWorker));
        assertFalse(validator.validate(otherWorker, worker));
    }

    @Test
    public void isValidWhenValidateTooCloseBirthdayRule() {
        Worker worker = new WorkerBuilder().build();
        Worker otherWorker = new WorkerBuilder().build();
        worker.setDateOfBirth(LocalDate.of(2000, 10, 1));
        otherWorker.setDateOfBirth(LocalDate.of(1990, 10, 20));
        workerService.save(worker);
        workerService.save(otherWorker);

        assertTrue(validator.validate(worker, otherWorker));
        assertTrue(validator.validate(otherWorker, worker));
    }

}
