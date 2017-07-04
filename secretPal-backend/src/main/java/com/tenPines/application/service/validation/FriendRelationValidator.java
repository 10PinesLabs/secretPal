package com.tenPines.application.service.validation;

import com.tenPines.application.clock.Clock;
import com.tenPines.application.service.FriendRelationService;
import com.tenPines.application.service.validation.rule.AssignationRule;
import com.tenPines.application.service.validation.rule.NotCircularRelationRule;
import com.tenPines.application.service.validation.rule.NotTheSamePersonRule;
import com.tenPines.application.service.validation.rule.NotTooCloseBirthdaysRule;
import com.tenPines.model.BirthdayPassedRule;
import com.tenPines.model.FriendRelation;
import com.tenPines.model.Worker;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

public class FriendRelationValidator {

    @Autowired
    public FriendRelationService friendRelationService;

    public List<AssignationRule> hardRules;
    public List<AssignationRule> rules;

    private final Clock clock;

    public   FriendRelationValidator(Clock clock, FriendRelationService friendRelationService) {
        this.clock = clock;
        this.friendRelationService = friendRelationService;
        this.rules = Arrays.asList(
                new NotCircularRelationRule(this.friendRelationService),
                new NotTooCloseBirthdaysRule(),
                new NotTheSamePersonRule(),
                new BirthdayPassedRule(clock)
        );
        this.hardRules = Arrays.asList();
    }

    public Boolean validate(Worker giver, Worker receiver) {
        return validateRules(giver, receiver) && !hasOtherSecretPal(giver, receiver);
    }

    private boolean validateRules(Worker giver, Worker receiver) {
        return validateHardRules(giver, receiver) || validateSoftRules(giver, receiver);
    }

    private Boolean hasOtherSecretPal(Worker giver, Worker receiver) {
        FriendRelation relation = friendRelationService.getByWorkerReceiver(receiver);
        return (relation != null) && (relation.getGiftGiver() != giver);
    }

    private Boolean validateHardRules(Worker giver, Worker receiver) {
        return hardRules.stream().anyMatch(rule -> rule.validate(giver, receiver));
    }

    private Boolean validateSoftRules(Worker giver, Worker receiver) {
        return rules.stream().allMatch(rule -> rule.validate(giver, receiver));
    }

    public boolean validateAll(List<Worker> validWorkers){
        return validWorkers.stream().allMatch(worker ->
                        validate(worker, getNextWorker(validWorkers, worker))
                );
    }

    private Worker getNextWorker(List<Worker> validWorkers, Worker worker) {
        return validWorkers.get(getIndexOfNextWorker(validWorkers, worker));
    }

    private int getIndexOfNextWorker(List<Worker> validWorkers, Worker worker) {
        return (validWorkers.indexOf(worker)+1) % validWorkers.size();
    }

}
