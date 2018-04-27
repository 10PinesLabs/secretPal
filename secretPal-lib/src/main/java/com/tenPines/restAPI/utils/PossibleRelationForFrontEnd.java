package com.tenPines.restAPI.utils;

import com.tenPines.application.service.FriendRelationService;
import com.tenPines.model.Worker;

import java.time.MonthDay;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PossibleRelationForFrontEnd {
    private Worker receiver;

    private Optional<Worker> giver;
    private List<Worker> posibleGivers;

    public PossibleRelationForFrontEnd(Worker participant, FriendRelationService friendRelationService) {
        this.receiver = participant;
        this.giver = friendRelationService.retrieveGiftGiverFor(participant);
        posibleGivers= new ArrayList();
    }

    public void updatePosibleGifters(FriendRelationService friendRelationService) {
        this.posibleGivers = friendRelationService.possibleGiftersFor(this.receiver);
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
