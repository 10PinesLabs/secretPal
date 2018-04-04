package com.tenPines.restAPI.utils;

import com.tenPines.application.service.FriendRelationService;
import com.tenPines.model.Worker;

import java.util.List;
import java.util.Optional;

public class ParticipantWithPosibilities {

    public Worker giver;
    public Optional<Worker> receiver;
    public List<Worker> posibleReceivers;

    public ParticipantWithPosibilities(Worker participant, FriendRelationService friendRelationService) {
        this.giver = participant;
        this.receiver = friendRelationService.retrieveGiftReceiverOf(participant);
        updatePosibleReceivers(friendRelationService);
    }

    private void updatePosibleReceivers(FriendRelationService friendRelationService) {
        this.posibleReceivers = friendRelationService.getAvailablesRelationsTo(this.giver);
    }

}
