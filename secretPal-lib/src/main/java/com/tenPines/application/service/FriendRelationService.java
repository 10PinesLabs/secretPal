package com.tenPines.application.service;

import com.tenPines.application.clock.Clock;
import com.tenPines.application.service.validation.FriendRelationValidator;
import com.tenPines.model.FriendRelation;
import com.tenPines.model.Hint;
import com.tenPines.model.Worker;
import com.tenPines.model.process.AssignmentException;
import com.tenPines.model.process.AutoAssignmentFunction;
import com.tenPines.model.process.RelationEstablisher;
import com.tenPines.persistence.FriendRelationRepository;
import com.tenPines.persistence.HintsRepository;
import com.tenPines.restAPI.utils.EmptyRelationForFrontEnd;
import com.tenPines.restAPI.utils.PossibleRelationForFrontEnd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.MonthDay;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.tenPines.model.process.AssignmentException.Reason.CANT_AUTO_ASSIGN;
import static com.tenPines.model.process.AssignmentException.Reason.NOT_ENOUGH_QUORUM;

@Service
public class FriendRelationService {

    private final Clock clock;
    private final FriendRelationRepository friendRelationRepository;
    private final HintsRepository hintsRepository;
    private final WorkerService workerService;
    private static Random random = new Random(System.nanoTime());
    private final CustomParticipantRuleService customParticipantRuleService;

    @Autowired
    public FriendRelationService(Clock clock, FriendRelationRepository friendRelationRepository, WorkerService workerService,
                                 CustomParticipantRuleService customParticipantRuleService, HintsRepository hintsRepository) {
        this.clock = clock;
        this.friendRelationRepository = friendRelationRepository;
        this.workerService = workerService;
        this.customParticipantRuleService = customParticipantRuleService;
        this.hintsRepository = hintsRepository;
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
                .orElseThrow(noHayAmigoAsignadoException())
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
                .map(FriendRelation::getGiftReceiver);
    }

    public Optional<Worker> retrieveGiftGiverFor(Worker worker) {
        return friendRelationRepository.findByGiftReceiver(worker)
                .map(FriendRelation::getGiftGiver);
    }



    public List<FriendRelation> allInmutableRelations() {
        return getAllRelations().stream().filter(this::inmutableRelation
        ).collect(Collectors.toList());
    }

    public List<Worker> workersWhoCanGive() {
        return workerService.getAllParticipants().stream().filter(this::canGive
        ).collect(Collectors.toList());
    }

    private Boolean canGive(Worker worker) {
        return friendRelationRepository.findByGiftGiver(worker)
                .map(relation -> !inmutableRelation(relation))
                .orElse(true);
    }

    public List<Worker> workersWhoCanReceive() {
        return workerService.getAllParticipants().stream().filter(this::canReceive
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

    public void updateRelation(Worker newGiver, Worker receiver) {
        Optional<FriendRelation> optionalRelation = friendRelationRepository.findByGiftReceiver(receiver);
        optionalRelation.ifPresent(relation -> changeGiver(relation, newGiver));
        optionalRelation.orElseGet(() -> create(newGiver, receiver));
    }

    private void changeGiver(FriendRelation relation, Worker newReceiver) {
        relation.setGiftGiver(newReceiver);
        friendRelationRepository.save(relation);
        return;
    }

    public void deleteAllRelations() {
        friendRelationRepository.deleteAllRelations();
    }

    public void deleteRelationsByGiftGivers(List<Worker> workers) {
        workers.forEach(friendRelationRepository::deleteByGiftGiver);
    }

    public void deleteByGiftGiver(Worker giver) {
        friendRelationRepository.deleteByGiftGiver(giver);
    }

    public Hint addHintFrom(Worker aWorkerGiver, Hint hint) {
        FriendRelation friendRelation = getByWorkerGiver(aWorkerGiver)
                .orElseThrow(noHayAmigoAsignadoException());
        friendRelation.addHint(hint);
        hintsRepository.save(hint);
        friendRelationRepository.save(friendRelation);
        return hint;
    }

    public void editHintFrom(Worker aWorkerGiver, long oldHintId, Hint newHint) {
        FriendRelation friendRelation = getByWorkerGiver(aWorkerGiver)
                .orElseThrow(noHayAmigoAsignadoException());
        Hint hintToEdit = friendRelation.hints().stream().filter(hint -> hint.getId() == oldHintId).findFirst().orElse(null);

        friendRelation.editHint(hintToEdit,newHint);
        friendRelationRepository.save(friendRelation);
    }


    public void removeHintFrom(Worker aWorkerGiver, Long hintId) {
        FriendRelation friendRelation = getByWorkerGiver(aWorkerGiver)
                .orElseThrow(noHayAmigoAsignadoException());
        Hint hintToRemove = hintsRepository.findOne(hintId);
        friendRelation.removeHint(hintToRemove);
        hintsRepository.delete(hintToRemove);
        friendRelationRepository.save(friendRelation);
    }

    public List<Hint> retrieveHintsGivenTo(Worker worker) {
        List<Hint> hints = new ArrayList<>();
        if(MonthDay.from(clock.now()).isAfter(worker.getBirthday())){
            hints = friendRelationRepository.findByGiftReceiver(worker).orElse(new EmptyRelationForFrontEnd()).hints();
        }
        return hints;
    }


    public List<Hint> retrieveHintsGivenBy(Worker worker) {
        return friendRelationRepository.findByGiftGiver(worker)
                .map(FriendRelation::hints)
                .orElseThrow(noHayPistasException());
    }

    public FriendRelation guessGiftGiverFor(Worker worker, String assumedGiftGiverFullName) {
        assertValidName(assumedGiftGiverFullName);
        FriendRelation relation = friendRelationRepository.findByGiftReceiver(worker)
                .orElseThrow(noHayAmigoAsignadoException());
        relation.guessGiftGiver(assumedGiftGiverFullName);
        friendRelationRepository.save(relation);
        return relation;
    }

    private void assertValidName(String assumedGiftGiverFullName) {
        if(assumedGiftGiverFullName == null ){
            throw new RuntimeException("No es un nombre valido para arriesgar");
        }
    }

    public Optional<Worker> getGiftSenderFor(Worker giftReceiver) {
        return friendRelationRepository.findByGiftReceiver(giftReceiver)
                .filter(FriendRelation::isGuessed)
                .map(FriendRelation::getGiftGiver);
    }

    public Optional<FriendRelation> guessStatusFor(Worker worker) {
        return friendRelationRepository.findByGiftReceiver(worker);
    }

    private Supplier<RuntimeException> noHayAmigoAsignadoException() {
        return () -> new RuntimeException("No hay amigo asignado!");
    }


    private Supplier<RuntimeException> noHayPistasException() {
        return () -> new RuntimeException("No hay pistas!");
    }

    public List<Worker> possibleGiftersFor(Worker receiver) {

        FriendRelationValidator validator = new FriendRelationValidator(clock, this, customParticipantRuleService);
        return workerService.getAllParticipants().stream().filter(participant ->
                validator.validatePossible(participant, receiver)
        ).collect(Collectors.toList());
    }

    public List<PossibleRelationForFrontEnd> allReceiversWithPosibilities() {
        return workerService.getAllParticipants().stream()
                .filter(this::notInImmutableRelation)
                .map(participant ->
                new PossibleRelationForFrontEnd(participant, this)
        ).collect(Collectors.toList());
    }

    private boolean notInImmutableRelation(Worker worker) {
        return friendRelationRepository.findByGiftReceiver(worker)
                .map(relation -> !inmutableRelation(relation))
                .orElse(true);
    }
}
