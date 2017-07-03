package com.tenPines.model;

import com.tenPines.application.service.FriendRelationService;
import com.tenPines.application.service.WorkerService;
import com.tenPines.builder.WorkerBuilder;
import com.tenPines.integration.SpringBaseTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Month;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class FriendRelationServiceTest extends SpringBaseTest {

    @Autowired
    private FriendRelationService friendRelationService;
    @Autowired
    private WorkerService workerService;

    private Worker aGiver;
    private Worker aReceiver;

    @Before
    public void setUp(){
        this.aGiver = new WorkerBuilder().build();
        this.aReceiver = new WorkerBuilder().buildFromDate(10, Month.NOVEMBER);
        workerService.save(aGiver);
        workerService.save(aReceiver);
    }

    @Test
    public void whenUpdateRelationOfGiverWITHExistingRelation(){
        friendRelationService.create(aGiver, aReceiver);

        Worker newReceiver = new WorkerBuilder().buildFromDate(22, Month.AUGUST);
        workerService.save(newReceiver);

        friendRelationService.updateRelation(aGiver, newReceiver);

        assertThat(friendRelationService.getAllRelations(), hasSize(1));
        assertThat(friendRelationService.retrieveAssignedFriendFor(aGiver), is(newReceiver));
    }

    @Test
    public void whenUpdateRelationOfGiverWITHOUTExistingRelation(){
        Worker newReceiver = new WorkerBuilder().buildFromDate(22, Month.AUGUST);
        workerService.save(newReceiver);

        friendRelationService.updateRelation(aGiver, newReceiver);

        assertThat(friendRelationService.getAllRelations(), hasSize(1));
        assertThat(friendRelationService.retrieveAssignedFriendFor(aGiver), is(newReceiver));
    }

}
