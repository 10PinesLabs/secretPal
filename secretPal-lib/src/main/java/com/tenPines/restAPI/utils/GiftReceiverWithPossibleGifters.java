package com.tenPines.restAPI.utils;

import com.tenPines.application.service.FriendRelationService;
import com.tenPines.model.Worker;

import java.util.List;
import java.util.Optional;

public class GiftReceiverWithPossibleGifters implements Comparable<GiftReceiverWithPossibleGifters>{
    private Worker receiver;

    private Optional<Worker> giver;
    private List<Worker> posibleGivers;

    public GiftReceiverWithPossibleGifters(Worker participant, FriendRelationService friendRelationService) {
        this.receiver = participant;
        this.giver = friendRelationService.retrieveGiftGiverFor(participant);
        updatePosibleReceivers(friendRelationService);
    }

    private void updatePosibleReceivers(FriendRelationService friendRelationService) {
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

    @Override
    public int compareTo(GiftReceiverWithPossibleGifters o) {
        boolean before = this.receiver.getBirthday().isBefore(o.getReceiver().getBirthday());
        int leq = 0;
        if(before){
            leq = -1;
        }
        return leq;
    }

    public Object receiverBirthday() {
        return receiver.getBirthday();
    }
}
