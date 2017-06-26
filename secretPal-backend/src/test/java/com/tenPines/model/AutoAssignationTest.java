package com.tenPines.model;

import com.tenPines.application.service.FriendRelationService;
import com.tenPines.application.service.WorkerService;
import com.tenPines.builder.WorkerBuilder;
import com.tenPines.integration.SpringBaseTest;
import com.tenPines.model.process.AssignmentException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

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
    public void When_there_is_more_than_2_participants_then_assign_a_secret_pal_for_everyone() throws Exception {
        Worker worker1 = new WorkerBuilder().build();
        worker1.setDateOfBirth(LocalDate.of(2000, 5, 1));
        workerService.save(worker1);
        Worker worker2 = new WorkerBuilder().build();
        worker2.setDateOfBirth(LocalDate.of(2000, 3, 29));
        workerService.save(worker2);
        Worker worker3 = new WorkerBuilder().build();
        worker3.setDateOfBirth(LocalDate.of(2000, 10, 22));
        workerService.save(worker3);

        friendRelationService.autoAssignRelations();

        assertThat(friendRelationService.getAllRelations(), hasSize(3));
        assertThat(friendRelationService.getAllRelations(), hasItem(hasProperty("giftGiver", is(worker1))));
        assertThat(friendRelationService.getAllRelations(), hasItem(hasProperty("giftGiver", is(worker2))));
        assertThat(friendRelationService.getAllRelations(), hasItem(hasProperty("giftGiver", is(worker3))));
    }

}