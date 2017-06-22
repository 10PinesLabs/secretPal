package com.tenPines.application.service.validation;

import com.tenPines.application.service.FriendRelationService;
import com.tenPines.application.service.validation.rule.AssignationRule;
import com.tenPines.application.service.validation.rule.NotCircularRelationRule;
import com.tenPines.application.service.validation.rule.NotTheSamePersonRule;
import com.tenPines.model.Worker;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FriendRelationValidator {

    @Autowired
    public FriendRelationService friendRelationService;

    public List<AssignationRule> hardRules;
    public List<AssignationRule> rules;

    public FriendRelationValidator(FriendRelationService friendRelationService) {
        this.friendRelationService = friendRelationService;
        this.rules = Arrays.asList(
                new NotCircularRelationRule(this.friendRelationService),
                new NotTheSamePersonRule()
        );
        this.hardRules = Arrays.asList();
    }

    public Boolean validate(Worker giver, Worker receiver) {
        return validateHardRules(giver, receiver) || validateSoftRules(giver, receiver);
    }

    private Boolean validateHardRules(Worker giver, Worker receiver) {
        return hardRules.stream().anyMatch(rule -> rule.validate(giver, receiver));
    }

    private Boolean validateSoftRules(Worker giver, Worker receiver) {
        return rules.stream().allMatch(rule -> rule.validate(giver, receiver));
    }

    public boolean validateAll(List<Worker> validWorkers){
        return validWorkers.stream().allMatch(worker -> validate(worker, getNextWorker(validWorkers, worker)));
    }

    private Worker getNextWorker(List<Worker> validWorkers, Worker worker) {
        return validWorkers.get(getIndexOfNextWorker(validWorkers, worker));
    }

    private int getIndexOfNextWorker(List<Worker> validWorkers, Worker worker) {
        return (validWorkers.indexOf(worker)+1) % validWorkers.size();
    }

}
