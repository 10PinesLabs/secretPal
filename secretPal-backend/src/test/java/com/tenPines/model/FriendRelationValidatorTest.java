package com.tenPines.model;


import com.tenPines.application.service.FriendRelationService;
import com.tenPines.application.service.WorkerService;
import com.tenPines.application.service.validation.FriendRelationValidator;
import com.tenPines.builder.WorkerBuilder;
import com.tenPines.integration.SpringBaseTest;
import com.tenPines.persistence.UserRepository;
import com.tenPines.persistence.WorkerRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

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
    public void whenTheReceiverWillGiftMeItShouldNotBeValid(){
        Worker giver = new WorkerBuilder().build();
        workerService.save(giver);
        Worker receiver = new WorkerBuilder().build();
        workerService.save(receiver);

        friendRelationService.create(giver, receiver);

        assertFalse(friendRelationValidator.validate(receiver,giver));
    }

}
