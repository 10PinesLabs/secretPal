package com.tenPines.application.service;

import com.tenPines.application.service.validation.FriendRelationValidator;
import com.tenPines.model.FriendRelation;
import com.tenPines.model.Worker;
import com.tenPines.model.process.AssignmentFunction;
import com.tenPines.model.process.RelationEstablisher;
import com.tenPines.persistence.FriendRelationRepository;
import com.tenPines.restAPI.utils.ParticipantWithPosibilities;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public void autoAssignRelations() {
        FriendRelationValidator validator = new FriendRelationValidator(this);
        List<Worker> validWorkers = assignableWorkers();

        for(int i=0;i<100;i++){
            Collections.shuffle(validWorkers, new Random(System.nanoTime()));
            if (validator.validateAll(validWorkers)) {
                deleteRelationsByGiftGivers(validWorkers);
                friendRelationRepository.save(
                    new AssignmentFunction(validWorkers).execute()
                );
            }
        }
    }

    private List<Worker> assignableWorkers() {
        return workerService.getAllParticipants().stream().filter(worker -> assignable(worker))
                .collect(Collectors.toList());
    }

    private Boolean assignable(Worker worker) {
        FriendRelation relation = friendRelationRepository.findByGiftGiver(worker);
        return (relation == null) || actualBirthday(relation.getGiftGiver()).isAfter(LocalDate.now());
    }

    private LocalDate actualBirthday(Worker worker) {
        return worker.getDateOfBirth().withYear(LocalDate.now().getYear());
    }

    public List<FriendRelation> getAllRelations() {
        return friendRelationRepository.findAll();
    }

    public Worker retrieveAssignedFriendFor(Worker unWorker) {
        FriendRelation aRelation = friendRelationRepository.findByGiftGiver(unWorker);
        if (aRelation == null) {
            autoAssignRelations();
            aRelation = friendRelationRepository.findByGiftGiver(unWorker);
        }
        return aRelation.getGiftReceiver();
    }

    public List<Worker> getAvailablesRelationsTo(Worker workerTo) {
        FriendRelationValidator validator = new FriendRelationValidator(this);
        return workerService.getAllParticipants().stream().filter(participant ->
            validator.validate(workerTo, participant)
        ).collect(Collectors.toList());
    }

    public FriendRelation getByWorkerReceiver(Worker receiver){
        return friendRelationRepository.findByGiftReceiver(receiver);
    }

    public void deleteRelationByReceipt(Worker to) {
        FriendRelation relation = friendRelationRepository.findByGiftReceiver(to);
        friendRelationRepository.delete(relation);
    }

    public void deleteAllRelations() {
        friendRelationRepository.deleteAllRelations();
    }

    public void deleteRelationsByGiftGivers(List<Worker> workers) {
        workers.stream().forEach(giver ->
            friendRelationRepository.deleteByGiftGiver(giver)
        );
    }

    public List<ParticipantWithPosibilities> allPosibilities() {
        return workerService.getAllParticipants().stream().map(participant ->
            new ParticipantWithPosibilities(participant, this)
        ).collect(Collectors.toList());
    }

    public void updateRelation(Worker giver, Worker newReceiver) {
        FriendRelation relation = friendRelationRepository.findByGiftGiver(giver);
        relation.setGiftReceiver(newReceiver);
        friendRelationRepository.save(relation);
    }
}
