package com.tenPines.restAPI.utils;

import com.tenPines.application.service.FriendRelationService;
import com.tenPines.model.Worker;

import java.time.MonthDay;
import java.util.List;
import java.util.Optional;

public class PossibleRelationForFrontEnd {
    private Worker receiver;

    private Optional<Worker> giver;
    private List<Worker> posibleGivers;

    public PossibleRelationForFrontEnd(Worker participant, FriendRelationService friendRelationService, List<Worker> workers) {
        this.receiver = participant;
        this.giver = friendRelationService.retrieveGiftGiverFor(participant);
        updatePosibleReceivers(friendRelationService, workers);
    }

    private void updatePosibleReceivers(FriendRelationService friendRelationService, List<Worker> workers) {
        this.posibleGivers = friendRelationService.possibleGiftersFor(this.receiver, workers);
    }

    public List<Worker> getPossibleGivers() {
        return posibleGivers;
    }

    public Optional<Worker> getGiver() {
        return giver;
    }

    public Worker getReceiver() {
        return receiver;
    }

    public int orderByBirthdayDate(PossibleRelationForFrontEnd otherGiftReceiver) {
        return this.receiverBirthday().compareTo(otherGiftReceiver.receiverBirthday());
    }

    public MonthDay receiverBirthday() {
        return receiver.getBirthday();
    }
}
