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

    public List<AssignationRule> rules;

    public FriendRelationValidator(FriendRelationService friendRelationService) {
        this.friendRelationService = friendRelationService;
        this.rules = Arrays.asList(
                new NotCircularRelationRule(this.friendRelationService)
        );
    }

    public Boolean validate(Worker giver, Worker receiver) {
        return rules.stream().allMatch(rule -> rule.validate(giver, receiver));
    }

}
