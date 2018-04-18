package com.tenPines.application.service.validation;

import com.tenPines.application.clock.Clock;
import com.tenPines.application.service.CustomParticipantRuleService;
import com.tenPines.application.service.FriendRelationService;
import com.tenPines.model.FriendRelation;
import com.tenPines.model.Worker;

import java.util.List;

public class FriendRelationValidator {

    private CustomParticipantRuleService customParticipantRuleService;
    public FriendRelationService friendRelationService;

    private final Clock clock;

    public FriendRelationValidator(Clock clock,
                                   FriendRelationService friendRelationService,
                                   CustomParticipantRuleService customParticipantRuleService) {
        this.clock = clock;
        this.friendRelationService = friendRelationService;
        this.customParticipantRuleService = customParticipantRuleService;
    }

    public Boolean validate(Worker giver, Worker receiver) {
        return validateRules(giver, receiver) && !hasOtherSecretPal(giver, receiver);
    }

    public Boolean validatePossible(Worker giver, Worker receiver) {
        return validateRules(giver, receiver) && !hasAssignedPal(giver, receiver);
    }

    private boolean hasAssignedPal(Worker giver, Worker receiver) {
        return friendRelationService.getByWorkerGiver(giver)
                .map(relation -> !relation.getGiftReceiver().equals(receiver))
                .orElse(false);
    }


    private Boolean validateRules(Worker giver, Worker receiver) {
        return customParticipantRuleService.getRules().stream().allMatch(rule -> rule.validate(giver, receiver));
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
        return customParticipantRuleService.getRules().stream().allMatch(rule -> rule.validate(relation, newRelations));
    }

}
