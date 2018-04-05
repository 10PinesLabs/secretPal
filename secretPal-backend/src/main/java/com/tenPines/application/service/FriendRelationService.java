package com.tenPines.application.service;

import com.tenPines.application.clock.Clock;
import com.tenPines.application.service.validation.FriendRelationValidator;
import com.tenPines.model.FriendRelation;
import com.tenPines.model.Worker;
import com.tenPines.model.process.AssignmentException;
import com.tenPines.model.process.AutoAssignmentFunction;
import com.tenPines.model.process.RelationEstablisher;
import com.tenPines.persistence.FriendRelationRepository;
import com.tenPines.restAPI.utils.ParticipantWithPosibilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.MonthDay;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static com.tenPines.model.process.AssignmentException.Reason.CANT_AUTO_ASSIGN;
import static com.tenPines.model.process.AssignmentException.Reason.NOT_ENOUGH_QUORUM;

@Service
public class FriendRelationService {

    private final Clock clock;
    private final FriendRelationRepository friendRelationRepository;
    private final WorkerService workerService;
    private static Random random = new Random(System.nanoTime());
    private final CustomParticipantRuleService customParticipantRuleService;

    @Autowired
    public FriendRelationService(Clock clock, FriendRelationRepository friendRelationRepository, WorkerService workerService,
                                 CustomParticipantRuleService customParticipantRuleService) {
        this.clock = clock;
        this.friendRelationRepository = friendRelationRepository;
        this.workerService = workerService;
        this.customParticipantRuleService = customParticipantRuleService;
    }

    public FriendRelation create(Worker friendWorker, Worker birthdayWorker) {
        return friendRelationRepository.save(new RelationEstablisher(friendWorker, birthdayWorker).createRelation());
    }

    public void autoAssignRelations() throws AssignmentException{
        checkIfThereAreEnoughParticipants();
        checkIfThereAreTwoParticipants();

        FriendRelationValidator validator = new FriendRelationValidator(clock, this, customParticipantRuleService);
        List<Worker> assignableWorkers = workersWhoCanGive();

        for (int i = 0; i<100; i++) {
            deleteRelationsByGiftGivers(workersWhoCanGive());
            List<FriendRelation> relations = new AutoAssignmentFunction(
                    clock, random, this, customParticipantRuleService).relate();

            if (allWorkersHasRelation(relations)) {
                friendRelationRepository.save(relations);
                break;
            }
        }
    }

    private Boolean allWorkersHasRelation(List<FriendRelation> newRelations) {
        return !newRelations.isEmpty() && (newRelations.size() == workersWhoCanGive().size());
    }

    private void checkIfThereAreEnoughParticipants() {
        if (workerService.getAllParticipants().size() < 2)
            throw new AssignmentException(NOT_ENOUGH_QUORUM);
    }

    private void checkIfThereAreTwoParticipants() {
        if (workerService.getAllParticipants().size() == 2)
            throw new AssignmentException(CANT_AUTO_ASSIGN);
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
        FriendRelationValidator validator = new FriendRelationValidator(clock, this, customParticipantRuleService);
        return workerService.getAllParticipants().stream().filter(participant ->
            validator.validate(workerTo, participant)
        ).collect(Collectors.toList());
    }

    public Optional<FriendRelation> getByWorkerGiver(Worker giver){
        return friendRelationRepository.findByGiftGiver(giver);
    }

    public Optional<FriendRelation> getByWorkerReceiver(Worker receiver){
        return friendRelationRepository.findByGiftReceiver(receiver);
    }

    public Optional<Worker> retrieveGiftReceiverOf(Worker worker) {
        return friendRelationRepository.findByGiftGiver(worker)
                .map(relation -> relation.getGiftReceiver());
    }

    public Worker retrieveGiftGiverFor(Worker worker) {
        return friendRelationRepository.findByGiftReceiver(worker)
                .map(relation -> relation.getGiftGiver()).get();
    }



    public List<ParticipantWithPosibilities> allPosibilities() {
        return workersWhoCanGive().stream().map(participant ->
            new ParticipantWithPosibilities(participant, this)
        ).collect(Collectors.toList());
    }

    public List<FriendRelation> allInmutableRelations() {
        return getAllRelations().stream().filter(relation ->
            inmutableRelation(relation)
        ).collect(Collectors.toList());
    }

    public List<Worker> workersWhoCanGive() {
        return workerService.getAllParticipants().stream().filter(worker ->
            canGive(worker)
        ).collect(Collectors.toList());
    }

    private Boolean canGive(Worker worker) {
        return friendRelationRepository.findByGiftGiver(worker)
                .map(relation -> !inmutableRelation(relation))
                .orElse(true);
    }

    public List<Worker> workersWhoCanReceive() {
        return workerService.getAllParticipants().stream().filter(worker ->
                canReceive(worker)
        ).collect(Collectors.toList());
    }

    private boolean canReceive(Worker worker) {
        return !lessThanTwoMonths(worker);
    }

    private LocalDate actualBirthday(Worker worker) {
        return worker.getDateOfBirth().withYear(clock.now().getYear());
    }

    public Boolean inmutableRelation(FriendRelation relation) {
        return lessThanTwoMonths(relation.getGiftReceiver());
    }

    private boolean lessThanTwoMonths(Worker worker) {
        MonthDay todayPlusTwoMonths = MonthDay.from(clock.now().plusMonths(2));
        MonthDay birthday = MonthDay.from(actualBirthday(worker));
        return birthday.equals(todayPlusTwoMonths) || birthday.isBefore(todayPlusTwoMonths);
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

    public void addHintFrom(Worker aWorkerGiver, String hint) {
        FriendRelation friendRelation = getByWorkerGiver(aWorkerGiver).get();
        friendRelation.addHint(hint);
        friendRelationRepository.save(friendRelation);
    }

    public void editHintFrom(Worker aWorkerGiver, String oldHint, String newHint) {
        FriendRelation friendRelation = getByWorkerGiver(aWorkerGiver).get();
        friendRelation.editHint(oldHint,newHint);
        friendRelationRepository.save(friendRelation);
    }


    public void removeHintFrom(Worker aWorkerGiver, String hint) {
        FriendRelation friendRelation = getByWorkerGiver(aWorkerGiver).get();
        friendRelation.removeHint(hint);
        friendRelationRepository.save(friendRelation);
    }

    public List<String> retrieveHintsGivenTo(Worker worker) {
        return friendRelationRepository.findByGiftReceiver(worker)
                .map(relation -> relation.hints()).get();
    }

    public List<String> retrieveHintsFrom(Worker worker) {
        return friendRelationRepository.findByGiftGiver(worker)
                .map(relation -> relation.hints()).get();
    }
}
