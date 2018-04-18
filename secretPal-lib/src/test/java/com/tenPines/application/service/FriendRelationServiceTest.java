package com.tenPines.application.service;

import com.tenPines.application.clock.FakeClock;
import com.tenPines.builder.WorkerBuilder;
import com.tenPines.integration.SpringBaseTest;
import com.tenPines.model.FriendRelation;
import com.tenPines.model.Hint;
import com.tenPines.model.Worker;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;


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

        Worker newGiver = new WorkerBuilder().buildFromDate(22, Month.AUGUST);
        workerService.save(newGiver);

        friendRelationService.updateRelation(newGiver, aWorkerReceiver);

        assertThat(friendRelationService.getAllRelations(), hasSize(1));
        assertThat(friendRelationService.retrieveAssignedFriendFor(newGiver), is(aWorkerReceiver));
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
    public void aWorkerCannotBeGiverIfItIsAlreadyAGiverInAnImmutableRelation() {
        Worker aWorker = new WorkerBuilder().buildFromDate(10, Month.NOVEMBER);
        Worker workerWhoHasBirthday = new WorkerBuilder().buildFromDate(1, Month.AUGUST);
        workerService.save(aWorker);
        workerService.save(workerWhoHasBirthday);
        FriendRelation relation = friendRelationService.create(aWorker, workerWhoHasBirthday);

        friendRelationService.makeImmutable(relation);

        List<Worker> assignableWorkers = friendRelationService.workersWhoCanGive();

        assertThat(assignableWorkers, hasSize(1));
        assertThat(assignableWorkers, hasItem(workerWhoHasBirthday));
    }

    @Test
    public void aWorkerCannotBeReceiverIfItIsAlreadyAReceiverInAnImmutableRelation() {
        Worker giver = new WorkerBuilder().buildFromDate(10, Month.NOVEMBER);
        Worker receiver = new WorkerBuilder().buildFromDate(1, Month.AUGUST);
        workerService.save(giver);
        workerService.save(receiver);
        FriendRelation relation = friendRelationService.create(giver, receiver);

        friendRelationService.makeImmutable(relation);

        List<Worker> assignableWorkers = friendRelationService.workersWhoCanReceive();
        assertThat(assignableWorkers, hasSize(1));
        assertThat(assignableWorkers, hasItem(giver));
    }

    @Test
    public void canFindTheGifterForAWorker(){
        setUp();
        friendRelationService.create(aWorkerGiver, aWorkerReceiver);
        assertThat(friendRelationService.retrieveGiftGiverFor(aWorkerReceiver).get(), is(aWorkerGiver));
    }

    @Test
    public void canSeeTheHintsGivenToTheGiftRecieverWhenItsBirthdayHasPassed(){
        setUp();
        clock.setTime(LocalDate.of(2018,02,01));
        Worker aWorkerReceiverWhoseBirthdayHasPassed = new WorkerBuilder()
                .withBirthDayDate(LocalDate.of(1995,03,04)).build();
        workerService.save(aWorkerReceiverWhoseBirthdayHasPassed);
        friendRelationService.create(aWorkerGiver, aWorkerReceiverWhoseBirthdayHasPassed);

        assertTrue(friendRelationService.retrieveHintsGivenTo(aWorkerReceiver).isEmpty());
    }

    @Test
    public void cannotSeeTheHintsGivenToTheGiftRecieverUntilItsBirthdayHasPassed(){
        setUp();
        clock.setTime(LocalDate.of(2018,02,01));
        Worker aWorkerReceiverWhoseBirthdayHasNotPassed = new WorkerBuilder()
                .withBirthDayDate(LocalDate.of(1995,01,01)).build();
        workerService.save(aWorkerReceiverWhoseBirthdayHasNotPassed);
        friendRelationService.create(aWorkerGiver, aWorkerReceiverWhoseBirthdayHasNotPassed);

        assertTrue(friendRelationService.retrieveHintsGivenTo(aWorkerReceiver).isEmpty());
    }

    @Test
    public void TheGifterCanAddHints(){
        setUp();
        friendRelationService.create(aWorkerGiver, aWorkerReceiver);
        Hint hint = new Hint("hint");
        friendRelationService.addHintFrom(aWorkerGiver, hint);
        assertThat(friendRelationService.retrieveHintsGivenBy(aWorkerGiver),hasSize(1));
        assertThat(friendRelationService.retrieveHintsGivenBy(aWorkerGiver).get(0)
                .message(), equalTo("hint"));
    }

    @Test
    public void TheGifterCanEditHints(){
        setUp();
        clock.setTime(LocalDate.of(2018,12,31));
        friendRelationService.create(aWorkerGiver, aWorkerReceiver);
        Hint hint = new Hint("hint");
        friendRelationService.addHintFrom(aWorkerGiver, hint);
        Hint newOne = new Hint("newOne");
        List<Hint> hints = friendRelationService.retrieveHintsGivenBy(aWorkerGiver);
        Hint oldHint = hints.stream()
                .filter(aHint -> aHint.message().equals(hint.message()))
                .findFirst().orElse(null);
        friendRelationService.editHintFrom(aWorkerGiver,oldHint.getId(), newOne);
        assertThat(friendRelationService.retrieveHintsGivenTo(aWorkerReceiver),hasSize(1));
        assertThat(friendRelationService.retrieveHintsGivenTo(aWorkerReceiver).get(0)
                .message(), equalTo("newOne"));
    }

    @Test
    public void TheGifterCanRemoveHints(){
        setUp();
        clock.setTime(LocalDate.of(2018,12,31));
        friendRelationService.create(aWorkerGiver, aWorkerReceiver);
        Hint hint = new Hint("hint");
        friendRelationService.addHintFrom(aWorkerGiver, hint);
        Hint oldHint = friendRelationService.retrieveHintsGivenTo(aWorkerReceiver).get(0);
        Long id = oldHint.getId();
        friendRelationService.removeHintFrom(aWorkerGiver, id);
        assertThat(friendRelationService.retrieveHintsGivenTo(aWorkerReceiver),hasSize(0));
    }

    @Test
    public void canGuessGiftGiverForAWorkerWhenItIsAlreadyAssigned(){
        setUp();
        friendRelationService.create(aWorkerGiver, aWorkerReceiver);
        FriendRelation friendRelationAfterGuess = friendRelationService.guessGiftGiverFor(aWorkerReceiver, aWorkerGiver.getFullName());
        assertThat(friendRelationAfterGuess.isGuessed(), is(true));
    }

    @Test
    public void cannotGuessGiftGiverForAWorkerWhenItIsNotYetAssigned(){
        setUp();
        try {
            friendRelationService.guessGiftGiverFor(aWorkerReceiver, "Some Name");
            fail("The exception was not raised");
        } catch (RuntimeException e) {
            assertThat(e.getMessage(), is("No hay amigo asignado!"));
        }
    }

    @Test
    public void gettingTheGiftGiverForAWorkerWhenItHasNotYetBeenGuessedReturnsNone(){
        setUp();
        friendRelationService.create(aWorkerGiver, aWorkerReceiver);
        assertThat(friendRelationService.getGiftSenderFor(aWorkerReceiver), is(Optional.empty()));
    }

    @Test
    public void gettingTheGiftGiverForAWorkerWhenItHasAlreadyBeenGuessedReturnsIt(){
        setUp();
        friendRelationService.create(aWorkerGiver, aWorkerReceiver);
        friendRelationService.guessGiftGiverFor(aWorkerReceiver, aWorkerGiver.getFullName());
        assertThat(friendRelationService.getGiftSenderFor(aWorkerReceiver), is(Optional.of(aWorkerGiver)));
    }

    @Test
    public void canObtainTheGuessesMadeInARelation(){
        setUp();
        friendRelationService.create(aWorkerGiver, aWorkerReceiver);

        String anotherWorker = new WorkerBuilder().build().getFullName();
        FriendRelation friendRelationAfterGuess = friendRelationService.guessGiftGiverFor(aWorkerReceiver, anotherWorker);
        assertThat(friendRelationAfterGuess.getGuessAttempts(), hasItem(anotherWorker));
    }

}
