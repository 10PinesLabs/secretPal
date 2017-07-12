package com.tenPines.application.service.validation;

import com.tenPines.application.service.CustomParticipantRuleService;
import com.tenPines.application.clock.Clock;
import com.tenPines.application.service.FriendRelationService;
import com.tenPines.application.service.validation.rule.AssignationRule;
import com.tenPines.application.service.validation.rule.NotTheSamePersonRule;
import com.tenPines.application.service.validation.rule.NotTooCloseBirthdaysRule;
import com.tenPines.model.BirthdayPassedRule;
import com.tenPines.model.FriendRelation;
import com.tenPines.model.Worker;

import java.util.Collections;
import java.util.List;

public class FriendRelationValidator {

    private CustomParticipantRuleService customParticipantRuleService;
    public FriendRelationService friendRelationService;

    private List<AssignationRule> hardRules;
    private List<AssignationRule> activeRules;

    public FriendRelationValidator(Clock clock, FriendRelationService friendRelationService,
                                   CustomParticipantRuleService customParticipantRuleService
                                   ) {
    private final Clock clock;

    public   FriendRelationValidator(Clock clock, FriendRelationService friendRelationService) {
        this.clock = clock;
        this.friendRelationService = friendRelationService;
        this.customParticipantRuleService = customParticipantRuleService;
        this.customParticipantRuleService.getActiveRules().add(new NotTheSamePersonRule());
        this.hardRules = Collections.emptyList();
        /* TODO: Agregar esto en donde corresponda
        new BirthdayPassedRule(clock)
         */
    }

    public Boolean validate(Worker giver, Worker receiver) {
        return validateRules(giver, receiver) && !hasOtherSecretPal(giver, receiver);
    }

    private boolean validateRules(Worker giver, Worker receiver) {
        return validateHardRules(giver, receiver) || validateSoftRules(giver, receiver);
    }

    private Boolean hasOtherSecretPal(Worker giver, Worker receiver) {
        return friendRelationService.getByWorkerReceiver(receiver)
                .map(relation -> !relation.getGiftGiver().equals(giver))
                .orElse(false);
    }

    private Boolean validateHardRules(Worker giver, Worker receiver) {
        return hardRules.stream().anyMatch(rule -> rule.validate(giver, receiver));
    }

    private Boolean validateSoftRules(Worker giver, Worker receiver) {
        activeRules = customParticipantRuleService.getActiveRules();
        activeRules.add(new NotTheSamePersonRule());
        return activeRules.stream().allMatch(rule -> rule.validate(giver, receiver));
    }

    public boolean validateAll(List<Worker> validWorkers) {
        return validWorkers.stream().allMatch(worker ->
                validate(worker, getNextWorker(validWorkers, worker))
        );
    }

    private Worker getNextWorker(List<Worker> validWorkers, Worker worker) {
        return validWorkers.get(getIndexOfNextWorker(validWorkers, worker));
    }

    private int getIndexOfNextWorker(List<Worker> validWorkers, Worker worker) {
        return (validWorkers.indexOf(worker) + 1) % validWorkers.size();
    }
}
