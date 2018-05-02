package com.tenPines.application.service;

import com.tenPines.application.clock.Clock;
import com.tenPines.application.service.validation.FriendRelationValidator;
import com.tenPines.model.FriendRelation;
import com.tenPines.model.Hint;
import com.tenPines.model.Worker;
import com.tenPines.model.process.RelationEstablisher;
import com.tenPines.persistence.FriendRelationRepository;
import com.tenPines.persistence.HintsRepository;
import com.tenPines.restAPI.utils.EmptyRelationForFrontEnd;
import com.tenPines.restAPI.utils.EmptyWorkerForFrontend;
import com.tenPines.restAPI.utils.PossibleRelationForFrontEnd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.MonthDay;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.lang.Integer.min;

@Service
public class FriendRelationService {

    public static final int AMOUNT_OF_RELATIONS_TO_PRELOAD = 5;
    private final Clock clock;
    private final FriendRelationRepository friendRelationRepository;
    private final HintsRepository hintsRepository;
    private final WorkerService workerService;
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

    public FriendRelation retrieveRelation(Long relationId) {
        return friendRelationRepository.getOne(relationId);
    }

    public FriendRelation create(Worker friendWorker, Worker birthdayWorker) {
        return friendRelationRepository.save(new RelationEstablisher(friendWorker, birthdayWorker).createRelation());
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

    public Optional<FriendRelation> getByWorkerGiver(Worker giver) {
        return friendRelationRepository.findByGiftGiver(giver);
    }

    public Optional<FriendRelation> getByWorkerReceiver(Worker receiver) {
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

    public List<FriendRelation> allImmutableRelations() {
        return getAllRelations().stream().filter(this::isImmutable).collect(Collectors.toList());
    }

    public List<Worker> workersWhoCanGive() {
        return workerService.getAllParticipants().stream().filter(this::canGive).collect(Collectors.toList());
    }

    private Boolean canGive(Worker worker) {
        return friendRelationRepository.findByGiftGiver(worker)
                .map(relation -> !isImmutable(relation))
                .orElse(true);
    }

    public List<Worker> workersWhoCanReceive() {
        return workerService.getAllParticipants().stream().filter(this::canReceive).collect(Collectors.toList());
    }

    private boolean canReceive(Worker worker) {
        return friendRelationRepository.findByGiftReceiver(worker)
                .map(relation -> !isImmutable(relation))
                .orElse(true);
    }

    public Boolean isImmutable(FriendRelation relation) {
        return relation.isImmutable();
    }

    public void makeImmutable(FriendRelation relation) {
        relation.makeImmutable();
        friendRelationRepository.save(relation);
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
        Hint hintToEdit = friendRelation.getHints().stream().filter(hint -> hint.getId() == oldHintId).findFirst().orElse(null);

        friendRelation.editHint(hintToEdit, newHint);
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
        return friendRelationRepository.findByGiftReceiver(worker)
                .filter(this::receiverBirthdayPassed)
                .orElse(new EmptyRelationForFrontEnd())
                .getHints();

    }

    private boolean receiverBirthdayPassed(FriendRelation r) {
        return birthdayHasPassed(r.getGiftReceiver());
    }


    private boolean birthdayHasPassed(Worker worker) {
        return MonthDay.from(clock.now()).isAfter(worker.getBirthday());
    }

    public List<Hint> retrieveHintsGivenBy(Worker worker) {
        return friendRelationRepository.findByGiftGiver(worker)
                .map(FriendRelation::getHints)
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

    public Worker getGiftSenderFor(Worker giftReceiver) {
        return friendRelationRepository.findByGiftReceiver(giftReceiver)
                .filter(FriendRelation::isGuessed)
                .map(FriendRelation::getGiftGiver).orElse(new EmptyWorkerForFrontend());
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
        return workerService.getAllParticipants().stream()
                .filter(participant ->
                        validator.validatePossible(participant, receiver)
                ).collect(Collectors.toList());
    }

    public List<PossibleRelationForFrontEnd> allReceiversWithPosibilities() {
        List<PossibleRelationForFrontEnd> possibleRelations = workerService.getAllParticipants().stream()
                .filter(this::canReceive)
                .map(participant ->
                        new PossibleRelationForFrontEnd(participant, this)
                ).collect(Collectors.toList());
        possibleRelations.sort(PossibleRelationForFrontEnd::orderByBirthdayDate);
        loadFirstFewRelationsOptions(possibleRelations);
        return possibleRelations;
    }

    private void loadFirstFewRelationsOptions(List<PossibleRelationForFrontEnd> possibleRelations) {
        for (int i = 0; i < min(AMOUNT_OF_RELATIONS_TO_PRELOAD, possibleRelations.size()); i++) {
            possibleRelations.get(i).updatePosibleGifters(this);
        }
    }

}
