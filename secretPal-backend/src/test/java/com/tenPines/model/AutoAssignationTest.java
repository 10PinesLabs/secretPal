package com.tenPines.model;

import com.tenPines.application.service.FriendRelationService;
import com.tenPines.application.service.WorkerService;
import com.tenPines.builder.WorkerBuilder;
import com.tenPines.integration.SpringBaseTest;
import com.tenPines.model.process.AssignmentException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class AutoAssignationTest extends SpringBaseTest {

    @Autowired
    private WorkerService workerService;
    @Autowired
    private FriendRelationService friendRelationService;

    @Test
    public void When_there_is_no_participants_then_it_should_raise_an_exception(){
        workerService.save(new WorkerBuilder().build());
        try {
            friendRelationService.autoAssignRelations();
            fail("The exception was not raised");
        } catch (AssignmentException e) {
            assertEquals(e.getReason(), AssignmentException.Reason.NOT_ENOUGH_QUORUM.toString());
            assertThat(friendRelationService.getAllRelations(), hasSize(0));
        }
    }
/*
    @Test
    public void When_there_is_2_participants_then_when_assign_each_participant_is_the_other_one_secret_pal() throws Exception {
        Worker worker = new WorkerBuilder().build();
        workerService.save(worker);

        Worker otherWorker = new WorkerBuilder().build();
        workerService.save(otherWorker);

        friendRelationService.autoAssignRelations();

        assertThat(friendRelationService.getAllRelations(), hasSize(2));
        assertThat(friendRelationService.getAllRelations(), hasItem(hasProperty("giftGiver", is(worker))));
        assertThat(friendRelationService.getAllRelations(), hasItem(hasProperty("giftGiver", is(otherWorker))));
    }
*/
    @Test
    public void When_there_is_more_than_2_participants_then_assign_a_secret_pal_for_everyone() throws Exception {
        Worker worker1 = new WorkerBuilder().build();
        workerService.save(worker1);
        Worker worker2 = new WorkerBuilder().build();
        workerService.save(worker2);
        Worker worker3 = new WorkerBuilder().build();
        workerService.save(worker3);

        friendRelationService.autoAssignRelations();

        assertThat(friendRelationService.getAllRelations(), hasSize(3));
        assertThat(friendRelationService.getAllRelations(), hasItem(hasProperty("giftGiver", is(worker1))));
        assertThat(friendRelationService.getAllRelations(), hasItem(hasProperty("giftGiver", is(worker2))));
        assertThat(friendRelationService.getAllRelations(), hasItem(hasProperty("giftGiver", is(worker3))));
    }

}