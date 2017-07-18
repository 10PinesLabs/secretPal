package com.tenPines.application.service.validation.rule;

import com.tenPines.application.service.FriendRelationService;
import com.tenPines.model.FriendRelation;
import com.tenPines.model.Worker;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotCircularRuleValidator {

    public static FriendRelationService friendRelationService;

    public NotCircularRuleValidator(FriendRelationService friendRelationService) {
        this.friendRelationService = friendRelationService;
    }

    public static Boolean validate(NotCircularRelationRule rule, Worker giver, Worker receiver) {
        return (rule.isActive && existACircularRelationBetween(giver, receiver)) || !rule.isActive;
    }

    private static Boolean existACircularRelationBetween(Worker giver, Worker receiver) {
        return friendRelationService.getByWorkerReceiver(giver)
                .map(relation -> !relation.getGiftGiver().equals(receiver))
                .orElse(true);
    }

    public static Boolean validate(NotCircularRelationRule rule, FriendRelation relation, List<FriendRelation> newRelations) {
        return validate(rule, relation.getGiftGiver(), relation.getGiftReceiver()) &&
                validateNewRelation(rule, relation, newRelations);
    }

    private static Boolean validateNewRelation(NotCircularRelationRule rule, FriendRelation relation, List<FriendRelation> newRelations) {
        return (rule.isActive && notNewCircularRelation(relation, newRelations)) || !rule.isActive;
    }

    private static Boolean notNewCircularRelation(FriendRelation newRelation, List<FriendRelation> newRelations) {
        return !newRelations.stream().anyMatch(thisRelation ->
                (thisRelation.getGiftReceiver() == newRelation.getGiftGiver()) &&
                        (thisRelation.getGiftGiver() == newRelation.getGiftReceiver())
        );
    }

}
