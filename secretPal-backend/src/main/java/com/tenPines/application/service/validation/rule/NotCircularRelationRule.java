package com.tenPines.application.service.validation.rule;

import com.tenPines.application.service.FriendRelationService;
import com.tenPines.model.FriendRelation;
import com.tenPines.model.Worker;

import java.util.List;

public class NotCircularRelationRule extends AssignationRule {

    private FriendRelationService friendRelationService;

    public NotCircularRelationRule(FriendRelationService friendRelationService) {
        this.friendRelationService = friendRelationService;
    }

    @Override
    public Boolean validate(Worker giver, Worker receiver) {
        return friendRelationService.getByWorkerReceiver(giver)
                .map(relation -> !relation.getGiftGiver().equals(receiver))
                .orElse(true);
    }

    @Override
    public Boolean validate(FriendRelation relation, List<FriendRelation> newRelations) {
        return validate(relation.getGiftGiver(), relation.getGiftReceiver()) &&
                notNewCircularRelation(relation, newRelations);
    }

    private Boolean notNewCircularRelation(FriendRelation newRelation, List<FriendRelation> newRelations) {
        return !newRelations.stream().anyMatch(thisRelation ->
                (thisRelation.getGiftReceiver() == newRelation.getGiftGiver()) &&
                        (thisRelation.getGiftGiver() == newRelation.getGiftReceiver())
        );
    }
}
