package com.tenPines.application.service;

import com.tenPines.application.ReminderSystem;
import com.tenPines.application.service.validator.FriendRelationValidator;
import com.tenPines.model.FriendRelation;
import com.tenPines.model.User;
import com.tenPines.model.Worker;
import com.tenPines.model.process.AssignmentException;
import com.tenPines.model.process.AssignmentFunction;
import com.tenPines.model.process.RelationEstablisher;
import com.tenPines.persistence.FriendRelationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.relation.Relation;
import java.time.Clock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class FriendRelationService {
    private final FriendRelationRepository friendRelationRepository;
    private final WorkerService workerService;

    public FriendRelationService(FriendRelationRepository friendRelationRepository, WorkerService workerService) {
        this.friendRelationRepository = friendRelationRepository;
        this.workerService = workerService;
    }

    public FriendRelation create(Worker friendWorker, Worker birthdayWorker) {
        return friendRelationRepository.save(new RelationEstablisher(friendWorker, birthdayWorker).createRelation());
    }

    public List<FriendRelation> getAllRelations() {
        return friendRelationRepository.findAll();
    }

    public Worker retrieveAssignedFriendFor(Worker unWorker) {
        FriendRelation aRelation = friendRelationRepository.findBygiftReceiver(unWorker);
        return aRelation.getGiftGiver();

        }

    public void deleteRelationByReceipt(Worker to) {
        FriendRelation relation = friendRelationRepository.findBygiftReceiver(to);
        friendRelationRepository.delete(relation);
    }

    public List<Worker> getAvailablesRelationsTo(Worker workerTo) {
        List<Worker> availablesReceipt = null;
        availablesReceipt.add(friendRelationRepository.findBygiftReceiver(workerTo).getGiftReceiver());
        return availablesReceipt;
    }

    public void deleteAllRelations() {
        friendRelationRepository.deleteAllRelations();
    }

    public void save(FriendRelation fr){
        friendRelationRepository.save(fr);
    }

    public FriendRelation getByWorkerReceiver(Worker receiver){
        return friendRelationRepository.findBygiftReceiver(receiver);
    }

    public List<FriendRelation> shuffle(List<User> users) {
        List<User> validUsers = users.stream().filter(u -> u.getWorker().getDateOfBirth().withYear(LocalDate.now().getYear()).isAfter(LocalDate.now())).collect(Collectors.toList());
        for(int i=0;i<100;i++){
            FriendRelationValidator friendRelationValidator = new FriendRelationValidator(this);
            if(friendRelationValidator.validateAll(validUsers)){
                return new AssignmentFunction(validUsers).execute();
            }
            Collections.shuffle(validUsers, new Random(System.nanoTime()));
        }

        for(int i=0;i<100;i++){
            FriendRelationValidator friendRelationValidator = new FriendRelationValidator(this);
            if(friendRelationValidator.validateHardRules(validUsers)){
                return new AssignmentFunction(validUsers).execute();
            }
            Collections.shuffle(validUsers, new Random(System.nanoTime()));
        }
        throw new RuntimeException("Cannot shuffle those users");
    }
}
