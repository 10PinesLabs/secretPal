package com.tenPines.model.process;

import com.tenPines.model.FriendRelation;
import com.tenPines.model.Worker;

import java.util.ArrayList;
import java.util.List;

public class AssignmentFunction {
    private final List<Worker> participants;

    public AssignmentFunction(List<Worker> participants) {
        this.participants = participants;
    }

    public List<FriendRelation> execute() throws AssignmentException{
        if(participants.size() < 2) {
            throw new AssignmentException(AssignmentException.Reason.NOT_ENOUGH_QUORUM);
        }
        List<FriendRelation> relations = new ArrayList<>();
        for (int i = 0; i < participants.size(); i++) {
            FriendRelation friendRelation = new FriendRelation(participants.get(i), participants.get((i + 1) % participants.size()));
            relations.add(friendRelation);
        }
        return relations;
    }
}
