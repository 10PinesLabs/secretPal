package com.tenPines.application.service;

import com.tenPines.application.service.validation.FriendRelationValidator;
import com.tenPines.model.FriendRelation;
import com.tenPines.model.Worker;
import com.tenPines.model.process.AssignmentException;
import com.tenPines.model.process.AssignmentFunction;
import com.tenPines.model.process.RelationEstablisher;
import com.tenPines.persistence.FriendRelationRepository;
import org.springframework.stereotype.Service;

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

    public List<FriendRelation> autoAssignRelations() {
        FriendRelationValidator validator = new FriendRelationValidator(this);
        List<Worker> validWorkers = workerService.getAllParticipants();

        for(int i=0;i<100;i++){
            Collections.shuffle(validWorkers, new Random(System.nanoTime()));
            if (validator.validateAll(validWorkers)) {
                List<FriendRelation> relations = new AssignmentFunction(validWorkers).execute();
                deleteAllRelations();/*TODO: Borrar solo las que se reasignan*/
                friendRelationRepository.save(relations);
                return relations;
            }
        }
        throw new AssignmentException(AssignmentException.Reason.CANT_AUTO_ASSIGN);
    }

    //TODO: filtrar solo aquellos workers que aun no regalan dentro de un mes este año
    private List<Worker> assignableWorkers() {
        return workerService.getAllParticipants().stream().filter(worker -> assignable(worker))
                .collect(Collectors.toList());
    }

    //TODO
    private Boolean assignable(Worker worker) {
        return null;
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
