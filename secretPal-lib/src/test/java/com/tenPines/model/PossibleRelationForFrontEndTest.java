package com.tenPines.model;

import com.tenPines.application.clock.FakeClock;
import com.tenPines.application.service.FriendRelationService;
import com.tenPines.application.service.WorkerService;
import com.tenPines.application.service.validation.rule.BirthdayPassedRule;
import com.tenPines.builder.WorkerBuilder;
import com.tenPines.integration.SpringBaseTest;
import com.tenPines.restAPI.utils.PossibleRelationForFrontEnd;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.Month;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Ignore
public class PossibleRelationForFrontEndTest extends SpringBaseTest{

    @Autowired
    private FriendRelationService friendRelationService;
    @Autowired
    private WorkerService workerService;
    @Autowired
    private FakeClock clock;
    private Worker worker;
    private Worker anotherWorker;
    private Worker yetAnotherWorker;
    private BirthdayPassedRule birthdayPassedRule;

    @Before
    public void setUp() {
        worker = new WorkerBuilder().buildFromDate(1, Month.NOVEMBER);
        anotherWorker = new WorkerBuilder().buildFromDate(1, Month.SEPTEMBER);
        yetAnotherWorker = new WorkerBuilder().buildFromDate(1, Month.AUGUST);
    }

    @Test
    public void whenIsTheOnlyParticipantPosibilitiesAreNone() {
        workerService.save(worker);

        PossibleRelationForFrontEnd posibilities =
                null; //new PossibleRelationForFrontEnd(worker, friendRelationService, workers);

        assertThat(posibilities.getReceiver(), is(worker));
        assertThat(posibilities.getPossibleGivers(), empty());
    }

    @Test
    public void whenThereAreMoreThanOneAssignableParticipantsPosibilitiesAreAllOtherParticipants() {
        clock.setTime(LocalDate.of(2017, Month.JANUARY, 1));
        workerService.save(worker);
        workerService.save(anotherWorker);
        workerService.save(yetAnotherWorker);

        PossibleRelationForFrontEnd posibilities =
                null; //new PossibleRelationForFrontEnd(worker, friendRelationService, workers);

        assertThat(posibilities.getReceiver(), is(worker));
        assertThat(posibilities.getPossibleGivers(), hasSize(2));
        assertThat(posibilities.getPossibleGivers(), hasItem(anotherWorker));
        assertThat(posibilities.getPossibleGivers(), hasItem(yetAnotherWorker));
    }

    @Test
    public void aParticipantThatIsAlreadyAssignedToSomeoneIsNotAssignable() {
        clock.setTime(LocalDate.of(2017, Month.JANUARY, 1));
        workerService.save(worker);
        workerService.save(anotherWorker);
        workerService.save(yetAnotherWorker);
        Worker newWorker = new WorkerBuilder().buildFromDate(1, Month.DECEMBER);
        workerService.save(newWorker);
        friendRelationService.create(yetAnotherWorker,newWorker);

        PossibleRelationForFrontEnd posibilities = null;
//                new PossibleRelationForFrontEnd(worker, friendRelationService, workers);

        assertThat(posibilities.getReceiver(), is(worker));
        assertThat(posibilities.getPossibleGivers(), hasSize(2));
        assertThat(posibilities.getPossibleGivers(), hasItem(anotherWorker));
        assertThat(posibilities.getPossibleGivers(), not(hasItem(yetAnotherWorker)));
        assertThat(posibilities.getPossibleGivers(), hasItem(newWorker));

    }

    @Test
    public void aParticipantThatIsAlreadyAssignedToSomeoneStillHasPossibilities() {
        clock.setTime(LocalDate.of(2017, Month.JANUARY, 1));
        workerService.save(worker);
        workerService.save(anotherWorker);
        workerService.save(yetAnotherWorker);
        Worker newWorker = new WorkerBuilder().buildFromDate(1, Month.DECEMBER);
        workerService.save(newWorker);
        friendRelationService.create(yetAnotherWorker,worker);

        PossibleRelationForFrontEnd posibilities = null;
                 //new PossibleRelationForFrontEnd(worker, friendRelationService, workers);

        assertThat(posibilities.getReceiver(), is(worker));
        assertThat(posibilities.getPossibleGivers(), hasSize(3));
        assertThat(posibilities.getPossibleGivers(), hasItem(anotherWorker));
        assertThat(posibilities.getPossibleGivers(), hasItem(yetAnotherWorker));
        assertThat(posibilities.getPossibleGivers(), hasItem(newWorker));

    }

    @Test
    public void aParticipantThatAlreadyIsInAnImmutableRelationHasNoPossibilities() {
        clock.setTime(LocalDate.of(2017, Month.DECEMBER, 1));
        workerService.save(worker);
        workerService.save(anotherWorker);

        PossibleRelationForFrontEnd posibilities = null;
//                new PossibleRelationForFrontEnd(worker, friendRelationService, workers);

        assertThat(posibilities.getReceiver(), is(worker));
        assertThat(posibilities.getPossibleGivers(), hasSize(0));


    }

}
