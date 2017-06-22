package com.tenPines.application.service.validation;

import com.tenPines.application.service.FriendRelationService;
import com.tenPines.application.service.validation.rule.AssignationRule;
import com.tenPines.application.service.validation.rule.NotCircularRelationRule;
import com.tenPines.model.Worker;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

public class FriendRelationValidator {

    @Autowired
    public FriendRelationService friendRelationService;

    public List<AssignationRule> hardRules;
    public List<AssignationRule> rules;

    public   FriendRelationValidator(FriendRelationService friendRelationService) {
        this.friendRelationService = friendRelationService;
        this.rules = Arrays.asList(
                new NotCircularRelationRule(this.friendRelationService));
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


}
