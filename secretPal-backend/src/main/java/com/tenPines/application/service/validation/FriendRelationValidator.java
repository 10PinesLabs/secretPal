package com.tenPines.application.service.validation;

import com.tenPines.application.service.CustomParticipantRuleService;
import com.tenPines.application.service.FriendRelationService;
import com.tenPines.application.service.validation.rule.AssignationRule;
import com.tenPines.application.service.validation.rule.NotTheSamePersonRule;
import com.tenPines.model.Worker;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FriendRelationValidator {

    private CustomParticipantRuleService customParticipantRuleService;
    public FriendRelationService friendRelationService;

    private List<AssignationRule> hardRules;
    private List<AssignationRule> rules;

    public FriendRelationValidator(FriendRelationService friendRelationService,
                                   CustomParticipantRuleService customParticipantRuleService
                                   ) {
        this.friendRelationService = friendRelationService;
        this.rules = customParticipantRuleService.getActiveRules();
        this.rules.add(new NotTheSamePersonRule());
        this.hardRules = Collections.emptyList();
    }

    public Boolean validate(Worker giver, Worker receiver) {
        return validateHardRules(giver, receiver) || validateSoftRules(giver, receiver);
    }

    private Boolean validateHardRules(Worker giver, Worker receiver) {
        return hardRules.stream().anyMatch(rule -> rule.validate(giver, receiver));
    }

    private Boolean validateSoftRules(Worker giver, Worker receiver) {
        List<AssignationRule> activeRules = rules.stream().filter(rule -> rule.isActive()).collect(Collectors.toList());
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
