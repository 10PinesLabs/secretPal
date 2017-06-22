package com.tenPines.model;

import com.tenPines.application.service.FriendRelationService;
import com.tenPines.application.service.WorkerService;
import com.tenPines.application.service.validation.FriendRelationValidator;
import com.tenPines.builder.WorkerBuilder;
import com.tenPines.integration.SpringBaseTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertFalse;

public class FriendRelationValidatorTest extends SpringBaseTest {

    public FriendRelationValidator friendRelationValidator;
    @Autowired
    public FriendRelationService friendRelationService;
    @Autowired
    public WorkerService workerService;

    @Before
    public void setUp(){
        friendRelationValidator = new FriendRelationValidator(friendRelationService);
    }

    @Test
    public void whenTheReceiverWillGiftTheGiverItShouldNotBeValid() {
        Worker giver = new WorkerBuilder().build();
        Worker receiver = new WorkerBuilder().build();
        workerService.save(giver);
        workerService.save(receiver);

        friendRelationService.create(giver, receiver);

        assertFalse(friendRelationValidator.validate(receiver,giver));
    }

    @Test
    public void whenTheReceiverIsTheSamePersonThanGiverItShouldNotBeValid() {
        Worker worker = new WorkerBuilder().build();
        workerService.save(worker);

        assertFalse(friendRelationValidator.validate(worker, worker));
    }

}
