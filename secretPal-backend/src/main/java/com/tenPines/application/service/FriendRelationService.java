package com.tenPines.application.service;

import com.tenPines.application.service.validation.FriendRelationValidator;
import com.tenPines.model.FriendRelation;
import com.tenPines.model.Worker;
import com.tenPines.model.process.AssignmentException;
import com.tenPines.model.process.AssignmentFunction;
import com.tenPines.model.process.RelationEstablisher;
import com.tenPines.persistence.FriendRelationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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

    public List<FriendRelation> autoAssignRelations() {
        FriendRelationValidator validator = new FriendRelationValidator(this);
        List<Worker> validWorkers = workerService.getAllParticipants();

        for(int i=0;i<100;i++){
            FriendRelationValidator friendRelationValidator = new FriendRelationValidator(this);
            Collections.shuffle(validWorkers, new Random(System.nanoTime()));
            if (friendRelationValidator.validateAll(validWorkers)) {
                List<FriendRelation> relations = new AssignmentFunction(validWorkers).execute();
                deleteAllRelations();
                friendRelationRepository.save(relations);
                return relations;
            }
        }
        throw new AssignmentException(AssignmentException.Reason.NOT_ENOUGH_QUORUM);
    }

    public List<FriendRelation> getAllRelations() {
        return friendRelationRepository.findAll();
    }

    public Worker retrieveAssignedFriendFor(Worker unWorker) {
        FriendRelation aRelation = friendRelationRepository.findBygiftReceiver(unWorker);
        if (aRelation == null) {
            autoAssignRelations();
            aRelation = friendRelationRepository.findBygiftReceiver(unWorker);
        }
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

    public FriendRelation getByWorkerReceiver(Worker receiver){
        return friendRelationRepository.findBygiftReceiver(receiver);
    }

}
