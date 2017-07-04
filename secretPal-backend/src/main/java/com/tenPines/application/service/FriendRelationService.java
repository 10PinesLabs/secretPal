package com.tenPines.application.service;

import com.tenPines.application.clock.Clock;
import com.tenPines.application.service.validation.FriendRelationValidator;
import com.tenPines.model.FriendRelation;
import com.tenPines.model.Worker;
import com.tenPines.model.process.AssignmentFunction;
import com.tenPines.model.process.RelationEstablisher;
import com.tenPines.persistence.FriendRelationRepository;
import com.tenPines.restAPI.utils.ParticipantWithPosibilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class FriendRelationService {

    private final Clock clock;
    private final FriendRelationRepository friendRelationRepository;
    private final WorkerService workerService;

    @Autowired
    public FriendRelationService(Clock clock, FriendRelationRepository friendRelationRepository, WorkerService workerService) {
        this.clock = clock;
        this.friendRelationRepository = friendRelationRepository;
        this.workerService = workerService;
    }

    public FriendRelation create(Worker friendWorker, Worker birthdayWorker) {
        return friendRelationRepository.save(new RelationEstablisher(friendWorker, birthdayWorker).createRelation());
    }

    public void autoAssignRelations() {
        FriendRelationValidator validator = new FriendRelationValidator(clock, this);
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


    public List<FriendRelation> getAllRelations() {
        return friendRelationRepository.findAll();
    }

    public Worker retrieveAssignedFriendFor(Worker unWorker) {
        return friendRelationRepository.findByGiftGiver(unWorker)
                .orElseThrow(() -> new RuntimeException("No hay amigo asignado!"))
                .getGiftReceiver();
    }

    public List<Worker> getAvailablesRelationsTo(Worker workerTo) {
        FriendRelationValidator validator = new FriendRelationValidator(clock, this);
        return workerService.getAllParticipants().stream().filter(participant ->
            validator.validate(workerTo, participant)
        ).collect(Collectors.toList());
    }

    public Optional<FriendRelation> getByWorkerReceiver(Worker receiver){
        return friendRelationRepository.findByGiftReceiver(receiver);
    }

    public void deleteAllRelations() {
        friendRelationRepository.deleteAllRelations();
    }

    public void deleteRelationsByGiftGivers(List<Worker> workers) {
        workers.stream().forEach(giver ->
            friendRelationRepository.deleteByGiftGiver(giver)
        );
    }

    public void deleteByGiftGiver(Worker giver) {
        friendRelationRepository.deleteByGiftGiver(giver);
    }

    public List<ParticipantWithPosibilities> allPosibilities() {
        return assignableWorkers().stream().map(participant ->
            new ParticipantWithPosibilities(participant, this)
        ).collect(Collectors.toList());
    }

    public List<FriendRelation> allInmutableRelations() {
        return getAllRelations().stream().filter(relation ->
            inmutableRelation(relation)
        ).collect(Collectors.toList());
    }

    public List<Worker> assignableWorkers() {
        return workerService.getAllParticipants().stream().filter(worker ->
            assignable(worker)
        ).collect(Collectors.toList());
    }

    private Boolean assignable(Worker worker) {
        return friendRelationRepository.findByGiftGiver(worker)
                .map(relation -> !inmutableRelation(relation))
                .orElse(true);
    }

    private LocalDate actualBirthday(Worker worker) {
        return worker.getDateOfBirth().withYear(clock.now().getYear());
    }

    public Boolean inmutableRelation(FriendRelation relation) {
        LocalDate actualBirthday = actualBirthday(relation.getGiftReceiver());
        return ChronoUnit.MONTHS.between(clock.now(), actualBirthday) < 1;
    }

    public void updateRelation(Worker giver, Worker newReceiver) {
        Optional<FriendRelation> optionalRelation = friendRelationRepository.findByGiftGiver(giver);
        optionalRelation.ifPresent(relation -> changeReceiver(relation, newReceiver));
        optionalRelation.orElseGet(() -> create(giver, newReceiver));
    }

    private void changeReceiver(FriendRelation relation, Worker newReceiver) {
        relation.setGiftReceiver(newReceiver);
        friendRelationRepository.save(relation);
    }

    public Optional<Worker> retrieveGiftReceiverOf(Worker worker) {
        return friendRelationRepository.findByGiftGiver(worker)
                .map(relation -> relation.getGiftReceiver());
    }

}
