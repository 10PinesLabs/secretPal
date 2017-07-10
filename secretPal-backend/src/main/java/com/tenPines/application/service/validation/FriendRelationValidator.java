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
    }

    public Boolean validate(Worker giver, Worker receiver) {
        return validateRules(giver, receiver) && !hasOtherSecretPal(giver, receiver);
    }


    private Boolean validateRules(Worker giver, Worker receiver) {
        return rules.stream().allMatch(rule -> rule.validate(giver, receiver));
    }

    private Boolean hasOtherSecretPal(Worker giver, Worker receiver) {
        return friendRelationService.getByWorkerReceiver(receiver)
                .map(relation -> !relation.getGiftGiver().equals(giver))
                .orElse(false);
    }

    public Boolean validateAll(List<FriendRelation> newRelations) {
        return newRelations.stream().allMatch(relation -> validateRelation(relation, newRelations));
    }

    private Boolean validateRelation(FriendRelation relation, List<FriendRelation> newRelations) {
        return rules.stream().allMatch(rule -> rule.validate(relation, newRelations));
    }

}
