package com.tenPines.model;

import com.tenPines.application.clock.FakeClock;
import com.tenPines.application.service.FriendRelationService;
import com.tenPines.application.service.WorkerService;
import com.tenPines.builder.WorkerBuilder;
import com.tenPines.integration.SpringBaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class FriendRelationServiceTest extends SpringBaseTest {

    @Autowired
    private FriendRelationService friendRelationService;
    @Autowired
    private WorkerService workerService;
    @Autowired
    private FakeClock clock;

    private Worker aWorkerGiver;
    private Worker aWorkerReceiver;

    public void setUp(){
        this.aWorkerGiver = new WorkerBuilder().build();
        this.aWorkerReceiver = new WorkerBuilder().build();
        workerService.save(aWorkerGiver);
        workerService.save(aWorkerReceiver);
    }

    @Test
    public void whenUpdateRelationOfGiverWITHExistingRelation(){
        setUp();
        friendRelationService.create(aWorkerGiver, aWorkerReceiver);

        Worker newReceiver = new WorkerBuilder().buildFromDate(22, Month.AUGUST);
        workerService.save(newReceiver);

        friendRelationService.updateRelation(aWorkerGiver, newReceiver);

        assertThat(friendRelationService.getAllRelations(), hasSize(1));
        assertThat(friendRelationService.retrieveAssignedFriendFor(aWorkerGiver), is(newReceiver));
    }

    @Test
    public void whenUpdateRelationOfGiverWITHOUTExistingRelation(){
        setUp();
        Worker newReceiver = new WorkerBuilder().buildFromDate(22, Month.AUGUST);
        workerService.save(newReceiver);

        friendRelationService.updateRelation(aWorkerGiver, newReceiver);

        assertThat(friendRelationService.getAllRelations(), hasSize(1));
        assertThat(friendRelationService.retrieveAssignedFriendFor(aWorkerGiver), is(newReceiver));
    }

    @Test
    public void whenAllWorkersCanChangeTheirRelationReceiver() {
        clock.setTime(LocalDate.of(2017, Month.FEBRUARY, 10)); //Set today
        Worker aWorker = new WorkerBuilder().buildFromDate(10, Month.NOVEMBER);
        Worker anotherWorker = new WorkerBuilder().buildFromDate(1, Month.AUGUST);
        workerService.save(aWorker);
        workerService.save(anotherWorker);

        List<Worker> assignableWorkers = friendRelationService.workersWhoCanGive();

        assertThat(assignableWorkers, hasSize(2));
        assertThat(assignableWorkers, hasItem(aWorker));
        assertThat(assignableWorkers, hasItem(anotherWorker));
    }

    @Test
    public void whenAWorkerCannotChangeTheirRelationReceiver() {
        clock.setTime(LocalDate.of(2017, Month.JUNE, 10)); //Set today

        Worker aWorker = new WorkerBuilder().buildFromDate(10, Month.NOVEMBER);
        Worker workerWhoHasBirthday = new WorkerBuilder().buildFromDate(1, Month.AUGUST);
        workerService.save(aWorker);
        workerService.save(workerWhoHasBirthday);

        friendRelationService.create(aWorker, workerWhoHasBirthday);
        clock.setTime(clock.now().plusMonths(3)); //advance time

        List<Worker> assignableWorkers = friendRelationService.workersWhoCanGive();

        assertThat(assignableWorkers, hasSize(1));
        assertThat(assignableWorkers, hasItem(workerWhoHasBirthday));
    }

    @Test
    public void whenItIsMoreThanTwoMonthsBeforeRelationBirthday() {
        clock.setTime(LocalDate.of(2017, Month.MAY, 10)); //Set today

        Worker aWorker = new WorkerBuilder().buildFromDate(10, Month.NOVEMBER);
        Worker anotherWorker = new WorkerBuilder().buildFromDate(1, Month.AUGUST);
        workerService.save(aWorker);
        workerService.save(anotherWorker);

        FriendRelation relation = friendRelationService.create(aWorker, anotherWorker);

        assertFalse(friendRelationService.inmutableRelation(relation));
    }

    @Test
    public void whenARelationBirthdayIsOver() {
        clock.setTime(LocalDate.of(2017, Month.JUNE, 10)); //Set today

        Worker aWorker = new WorkerBuilder().buildFromDate(10, Month.NOVEMBER);
        Worker anotherWorker = new WorkerBuilder().buildFromDate(1, Month.AUGUST);
        workerService.save(aWorker);
        workerService.save(anotherWorker);

        FriendRelation relation = friendRelationService.create(aWorker, anotherWorker);
        clock.setTime(LocalDate.of(2017, Month.AUGUST, 5)); //advance time

        assertTrue(friendRelationService.inmutableRelation(relation));
    }

    @Test
    public void whenARelationBirthdayWillPassInLessThanOneMonth() {
        clock.setTime(LocalDate.of(2017, Month.JUNE, 10)); //Set today

        Worker aWorker = new WorkerBuilder().buildFromDate(20, Month.NOVEMBER);
        Worker anotherWorker = new WorkerBuilder().buildFromDate(1, Month.AUGUST);
        workerService.save(aWorker);
        workerService.save(anotherWorker);

        FriendRelation relation = friendRelationService.create(aWorker, anotherWorker);
        clock.setTime(LocalDate.of(2017, Month.JULY, 25)); //advance time

        assertTrue(friendRelationService.inmutableRelation(relation));
    }

    @Test
    public void whenAllWorkersCanBeReceivers() {
        clock.setTime(LocalDate.of(2017, Month.FEBRUARY, 10)); //Set today
        Worker aWorker = new WorkerBuilder().buildFromDate(10, Month.NOVEMBER);
        Worker anotherWorker = new WorkerBuilder().buildFromDate(1, Month.AUGUST);
        workerService.save(aWorker);
        workerService.save(anotherWorker);

        List<Worker> assignableWorkers = friendRelationService.workersWhoCanReceive();

        assertThat(assignableWorkers, hasSize(2));
        assertThat(assignableWorkers, hasItem(aWorker));
        assertThat(assignableWorkers, hasItem(anotherWorker));
    }

    @Test
    public void whenAWorkerCannotBeReceiver() {
        clock.setTime(LocalDate.of(2017, Month.JUNE, 10)); //Set today

        Worker aWorker = new WorkerBuilder().buildFromDate(10, Month.NOVEMBER);
        Worker workerWhoHasBirthday = new WorkerBuilder().buildFromDate(1, Month.AUGUST);
        workerService.save(aWorker);
        workerService.save(workerWhoHasBirthday);

        friendRelationService.create(aWorker, workerWhoHasBirthday);
        clock.setTime(clock.now().plusMonths(2)); //advance time

        List<Worker> assignableWorkers = friendRelationService.workersWhoCanReceive();

        assertThat(assignableWorkers, hasSize(1));
        assertThat(assignableWorkers, hasItem(aWorker));
    }

    @Test
    public void canFindTheGifterForAWorker(){
        setUp();
        friendRelationService.create(aWorkerGiver, aWorkerReceiver);
        assertThat(friendRelationService.retrieveGiftGiverFor(aWorkerReceiver), is(aWorkerGiver));
    }

}
