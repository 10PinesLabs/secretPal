package com.tenPines.model.process;

import com.tenPines.application.clock.Clock;
import com.tenPines.application.service.FriendRelationService;
import com.tenPines.application.service.validation.FriendRelationValidator;
import com.tenPines.model.FriendRelation;
import com.tenPines.model.Worker;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;


public class AutoAssignmentFunction {

    private final Clock clock;
    private final Random random;
    private final FriendRelationService friendRelationService;
    private final FriendRelationValidator validator;
    private final List<Worker> givers;
    private final List<Worker> receivers;


    public AutoAssignmentFunction(Clock clock, Random random, FriendRelationService friendRelationService) {
        this.clock = clock;
        this.random = random;
        this.friendRelationService = friendRelationService;
        this.validator = new FriendRelationValidator(clock, friendRelationService);
        this.givers = friendRelationService.workersWhoCanGive();
        this.receivers = friendRelationService.workersWhoCanReceive();
    }

    public List<FriendRelation> relate() {
        List<FriendRelation> newRelations = createNewRelations();
        if (validator.validateAll(newRelations))
            return newRelations;
        return Arrays.asList();
    }

    private List<FriendRelation> createNewRelations() {
        shuffleReceivers();
        return givers.stream().map(giver ->
                new FriendRelation(giver, receivers.get(givers.indexOf(giver)))
        ).collect(Collectors.toList());
    }

    private void shuffleReceivers() {
        Collections.shuffle(receivers, random);
    }


}
