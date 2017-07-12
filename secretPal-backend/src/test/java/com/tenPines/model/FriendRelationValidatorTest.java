package com.tenPines.model;

import com.tenPines.application.clock.Clock;
import com.tenPines.application.service.CustomParticipantRuleService;
import com.tenPines.application.service.FriendRelationService;
import com.tenPines.application.service.WorkerService;
import com.tenPines.application.service.validation.FriendRelationValidator;
import com.tenPines.application.service.validation.rule.NotCircularRelationRule;
import com.tenPines.application.service.validation.rule.NotTooCloseBirthdaysRule;
import com.tenPines.application.service.validation.rule.RuleValidator;
import com.tenPines.builder.WorkerBuilder;
import com.tenPines.integration.SpringBaseTest;
import com.tenPines.persistence.NotCircularRelationRuleRepository;
import com.tenPines.persistence.NotTooCloseBirthdayRuleRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class FriendRelationValidatorTest extends SpringBaseTest {

    @Autowired
    private Clock clock;
    @Autowired
    private FriendRelationService friendRelationService;
    @Autowired
    public WorkerService workerService;
    @Autowired
    private NotCircularRelationRuleRepository notCircularRelationRuleRepository;
    @Autowired
    private NotTooCloseBirthdayRuleRepository notTooCloseBirthdayRuleRepository;
    @Autowired
    private CustomParticipantRuleService customParticipantRuleService;
    @Autowired
    private RuleValidator ruleValidator;

    @Before
    public void setUp() {
    }
    /*TODO: Ver esto
    @Before
    public void setUp(){
        validator = new FriendRelationValidator(clock, friendRelationService);
    }
     */

    @Test
    public void whenTheReceiverWillGiftTheGiverItShouldNotBeValid() {
        NotCircularRelationRule notCircularRelationRule = notCircularRelationRuleRepository.findAll().get(0);
        notCircularRelationRule.isActive = false;
        notCircularRelationRuleRepository.save(notCircularRelationRule);

        notCircularRelationRule.changeRuleIntention();
        notCircularRelationRuleRepository.save(notCircularRelationRule);

        Worker giver = new WorkerBuilder().build();
        Worker receiver = new WorkerBuilder().build();

        workerService.save(giver);
        workerService.save(receiver);

        friendRelationService.create(giver, receiver);

        assertFalse(new FriendRelationValidator(clock, friendRelationService, customParticipantRuleService).validate(receiver, giver));
    }

    @Test
    public void whenTheReceiverIsTheSamePersonThanGiverItShouldNotBeValid() {
        Worker worker = new WorkerBuilder().build();
        workerService.save(worker);

        assertFalse(new FriendRelationValidator(clock, friendRelationService, customParticipantRuleService).validate(worker, worker));
    }

    @Test
    public void isInvalidWhenValidateTooCloseBirthdayRule() {
        NotTooCloseBirthdaysRule notTooCloseBirthdaysRule = notTooCloseBirthdayRuleRepository.findAll().get(0);
        notTooCloseBirthdaysRule.isActive = false;
        notTooCloseBirthdayRuleRepository.save(notTooCloseBirthdaysRule);

        notTooCloseBirthdaysRule.changeRuleIntention();
        notTooCloseBirthdayRuleRepository.save(notTooCloseBirthdaysRule);

        Worker worker = new WorkerBuilder().build();
        Worker otherWorker = new WorkerBuilder().build();
        worker.setDateOfBirth(LocalDate.of(2000, 5, 1));
        otherWorker.setDateOfBirth(LocalDate.of(1990, 5, 8));
        workerService.save(worker);
        workerService.save(otherWorker);

        assertFalse(new FriendRelationValidator(clock, friendRelationService, customParticipantRuleService).validate(worker, otherWorker));
        assertFalse(new FriendRelationValidator(clock, friendRelationService, customParticipantRuleService).validate(otherWorker, worker));
    }

    @Test
    public void isValidWhenValidateTooCloseBirthdayRule() {
        NotTooCloseBirthdaysRule notTooCloseBirthdaysRule = notTooCloseBirthdayRuleRepository.findAll().get(0);
        notTooCloseBirthdaysRule.isActive = false;
        notTooCloseBirthdayRuleRepository.save(notTooCloseBirthdaysRule);

        notTooCloseBirthdaysRule.changeRuleIntention();
        notTooCloseBirthdayRuleRepository.save(notTooCloseBirthdaysRule);

        Worker worker = new WorkerBuilder().build();
        Worker otherWorker = new WorkerBuilder().build();
        worker.setDateOfBirth(LocalDate.of(2000, 10, 1));
        otherWorker.setDateOfBirth(LocalDate.of(1990, 10, 20));
        workerService.save(worker);
        workerService.save(otherWorker);

        assertTrue(new FriendRelationValidator(clock, friendRelationService, customParticipantRuleService).validate(worker, otherWorker));
        assertTrue(new FriendRelationValidator(clock, friendRelationService, customParticipantRuleService).validate(otherWorker, worker));
    }

}
