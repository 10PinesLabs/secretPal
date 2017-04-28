package com.tenPines.model.process;

import com.tenPines.model.FriendRelation;
import com.tenPines.model.User;
import com.tenPines.model.Worker;

import java.util.List;
import java.util.stream.Collectors;

public class AssignmentFunction {
    private final List<Worker> participants;

    public AssignmentFunction(List<User> validUsers) {
        this.participants = validUsers.stream()
                .map(u->u.getWorker())
                .collect(Collectors.toList());
    }

    public List<FriendRelation> execute() throws AssignmentException{
        if(participants.size() < 2) {
            throw new AssignmentException(AssignmentException.Reason.NOT_ENOUGH_QUORUM);
        }
        return participants.stream().map(p -> new FriendRelation(p,
                getNextWorker(p)))
                .collect(Collectors.toList());
    }

    private Worker getNextWorker(Worker p) {
        return participants.get(getIndexOfNextWorker(p));
    }

    private int getIndexOfNextWorker(Worker p) {
        return (participants.indexOf(p) + 1) % participants.size();
    }
}
