package com.tenPines.model;

import com.tenPines.application.clock.FakeClock;
import com.tenPines.application.service.FriendRelationService;
import com.tenPines.application.service.WorkerService;
import com.tenPines.builder.WorkerBuilder;
import com.tenPines.integration.SpringBaseTest;
import com.tenPines.model.process.AssignmentException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class AutoAssignmentTest extends SpringBaseTest {

    @Autowired
    private FriendRelationService friendRelationService;
    @Autowired
    private WorkerService workerService;
    @Autowired
    private FakeClock clock;

    private Worker worker;
    private Worker otherWorker;
    private Worker yetOtherWorker;
    private Worker anotherWorker;

    @Before
    public void setUp(){
        clock.setTime(LocalDate.of(2017, Month.JANUARY, 1));
        worker = new WorkerBuilder().buildFromDate(1, Month.JUNE);
        otherWorker = new WorkerBuilder().buildFromDate(1, Month.SEPTEMBER);
        yetOtherWorker = new WorkerBuilder().buildFromDate(1, Month.AUGUST);
        anotherWorker = new WorkerBuilder().buildFromDate(1, Month.OCTOBER);
    }


    private void assertRelated(Worker worker) {
        assertExistsRelationGiver(worker);
        assertExistsRelationReceiver(worker);
    }

    private void assertExistsRelationGiver(Worker worker) {
        assertThat(friendRelationService.getAllRelations(), hasItem(
                hasProperty("giftGiver", is(worker))
        ));
    }

    private void assertExistsRelationReceiver(Worker worker) {
        assertThat(friendRelationService.getAllRelations(), hasItem(
                hasProperty("giftReceiver", is(worker))
        ));
    }

    private Boolean relationHasChanged(FriendRelation relation) {
        Worker newReceiver = friendRelationService.retrieveAssignedFriendFor(relation.getGiftGiver());
        return !relation.getGiftReceiver().equals(newReceiver);
    }

    @Test
    public void whenThereIsntAnyParticipantThenAutoAssignRaiseQuorumError() throws AssignmentException {
        try {
            friendRelationService.autoAssignRelations();
        } catch (AssignmentException e) {
            assertEquals(e.getReason(), AssignmentException.Reason.NOT_ENOUGH_QUORUM.toString());
        }
        assertThat(friendRelationService.getAllRelations(), empty());
    }

    @Test
    public void whenThereIsOnlyOneParticipantThenAutoAssignRaiseQuorumError() throws AssignmentException {
        workerService.save(new WorkerBuilder().build());

        try {
            friendRelationService.autoAssignRelations();
        } catch (AssignmentException e) {
            assertEquals(e.getReason(), AssignmentException.Reason.NOT_ENOUGH_QUORUM.toString());
        }
        assertThat(friendRelationService.getAllRelations(), empty());
    }

    @Test
    public void whenThereIsTwoParticipantsThenAutoAssignRaiseRuleError() throws AssignmentException {
        workerService.save(new WorkerBuilder().build());
        workerService.save(new WorkerBuilder().build());

        try {
            friendRelationService.autoAssignRelations();
        } catch (AssignmentException e) {
            assertEquals(e.getReason(), AssignmentException.Reason.CANT_AUTO_ASSIGN.toString());
        }
        assertThat(friendRelationService.getAllRelations(), empty());
    }

    @Test
    public void whenThereAreMoreThanTwoParticipantsThenAutoAssignRelatesThemAll() throws AssignmentException {
        workerService.save(worker);
        workerService.save(otherWorker);
        workerService.save(yetOtherWorker);

        friendRelationService.autoAssignRelations();

        assertRelated(worker);
        assertRelated(otherWorker);
        assertRelated(yetOtherWorker);
    }
}
