package com.tenPines.model.process;

import com.tenPines.model.FriendRelation;
import com.tenPines.model.Worker;

import static com.tenPines.model.process.AssignmentException.Reason.*;

public class RelationEstablisher {
    private final Worker giftgiver;
    private final Worker giftReciever;

    public RelationEstablisher(Worker giftgiver, Worker giftReciever) {
        this.giftgiver = giftgiver;
        this.giftReciever = giftReciever;
    }

    public FriendRelation createRelation() {
        assertReceiverIsNotNull(giftReciever);
        assertGiverIsNotNull(giftgiver);
        checkIfWantToParticipate(giftgiver);
        checkIfWantToParticipate(giftReciever);
        checkIfNotTheSameParticipant(giftgiver, giftReciever);

        return relate();
    }

    public void assertReceiverIsNotNull(Worker giftReceiver) {
        if(giftReceiver == null){
            throw new AssignmentException(RECEIVER_NULL);
        }
    }

    public void assertGiverIsNotNull(Worker giftGiver) {
        if(giftGiver == null){
            throw new AssignmentException(GIVER_NULL);
        }
    }

    private FriendRelation relate() {
        return new FriendRelation(giftgiver, giftReciever);
    }

    private void checkIfNotTheSameParticipant(Worker participant, Worker secretPal) {
        if(participant.equals(secretPal))
            throw new AssignmentException(CANT_SELF_ASSIGN);
    }
    private void checkIfWantToParticipate(Worker worker) {
        if(!worker.getWantsToParticipate())
            throw new AssignmentException(AssignmentException.Reason.DOES_NOT_WANT_TO_PARTICIPATE, worker);
    }
}
