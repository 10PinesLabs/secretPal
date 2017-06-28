package com.tenPines.restAPI.utils;

import com.tenPines.application.service.FriendRelationService;
import com.tenPines.model.Worker;

import java.util.List;

public class ParticipantWithPosibilities {

    public Worker giver;
    public Worker receiver;
    public List<Worker> posibleReceivers;

    public ParticipantWithPosibilities(Worker participant, FriendRelationService friendRelationService) {
        this.giver = participant;
        this.receiver = friendRelationService.retrieveAssignedFriendFor(participant);
        updatePosibleReceivers(friendRelationService);
    }

    private void updatePosibleReceivers(FriendRelationService friendRelationService) {
        this.posibleReceivers = friendRelationService.getAvailablesRelationsTo(this.giver);
    }

}
