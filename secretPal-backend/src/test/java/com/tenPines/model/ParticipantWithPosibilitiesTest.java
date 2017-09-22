package com.tenPines.model;

import com.tenPines.application.clock.FakeClock;
import com.tenPines.application.service.FriendRelationService;
import com.tenPines.application.service.WorkerService;
import com.tenPines.application.service.validation.rule.BirthdayPassedRule;
import com.tenPines.builder.WorkerBuilder;
import com.tenPines.integration.SpringBaseTest;
import com.tenPines.restAPI.utils.ParticipantWithPosibilities;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.Month;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ParticipantWithPosibilitiesTest extends SpringBaseTest {

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

        ParticipantWithPosibilities posibilities =
                new ParticipantWithPosibilities(worker, friendRelationService);

        assertThat(posibilities.giver, is(worker));
        assertThat(posibilities.posibleReceivers, empty());
    }

    @Test
    public void whenThereAreMoreThanOneAssignableParticipantsPosibilitiesAreAllOtherParticipants() {
        clock.setTime(LocalDate.of(2017, Month.JANUARY, 1));
        workerService.save(worker);
        workerService.save(anotherWorker);
        workerService.save(yetAnotherWorker);

        ParticipantWithPosibilities posibilities =
                new ParticipantWithPosibilities(worker, friendRelationService);

        assertThat(posibilities.giver, is(worker));
        assertThat(posibilities.posibleReceivers, hasSize(2));
        assertThat(posibilities.posibleReceivers, hasItem(anotherWorker));
        assertThat(posibilities.posibleReceivers, hasItem(yetAnotherWorker));
    }

    @Test
    public void whenThereAreAtleastOneUnassignableParticipantPosibilitiesAreAllTheAssignableParticipants() {
        clock.setTime(LocalDate.of(2017, Month.MAY, 1));
        Worker unassignableWorker = new WorkerBuilder().buildFromDate(1, Month.MARCH);
        workerService.save(worker);
        workerService.save(anotherWorker);
        workerService.save(unassignableWorker);

        ParticipantWithPosibilities posibilities =
                new ParticipantWithPosibilities(worker, friendRelationService);

        assertThat(posibilities.giver, is(worker));
        assertThat(posibilities.posibleReceivers, hasSize(1));
        assertThat(posibilities.posibleReceivers, hasItem(anotherWorker));
    }

}
