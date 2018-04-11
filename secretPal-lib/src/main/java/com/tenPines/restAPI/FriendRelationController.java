package com.tenPines.restAPI;

import com.tenPines.application.SystemPalFacade;
import com.tenPines.application.service.FriendRelationService;
import com.tenPines.application.service.WorkerService;
import com.tenPines.model.FriendRelation;
import com.tenPines.model.Hint;
import com.tenPines.model.Worker;
import com.tenPines.restAPI.utils.GuessResponse;
import com.tenPines.restAPI.utils.ParticipantWithPosibilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/api/friendRelation")
public class FriendRelationController {

    @Autowired
    private SystemPalFacade systemFacade;

    @Autowired
    private FriendRelationService friendRelationService;

    @Autowired
    private WorkerService workerService;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<FriendRelation> workersWithFriends() {
        return systemFacade.getAllRelations();
    }

    @RequestMapping(value = "/posibleFriend/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<Worker> posiblesFriends(@PathVariable Long id) {
        return systemFacade.getPossibleFriendsTo(id);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public void createRelation(@RequestBody @Valid List<FriendRelation> friendRelations) {
        systemFacade.deleteAllRelations();
        for (FriendRelation friendRelation : friendRelations) {
            systemFacade.createRelation(friendRelation.getGiftGiver(), friendRelation.getGiftReceiver());
        }
    }

    @RequestMapping(value = "/{from}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteRelation(@PathVariable Long from) {
        Worker giver = workerService.retrieveWorker(from);
        systemFacade.deleteRelation(giver);
    }

    @RequestMapping(value = "/friend/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Worker retrieveGiftee(@PathVariable("id") Long id) {
        return systemFacade.retrieveAssignedFriendFor(id);
    }

    @RequestMapping(value = "/autoAssign", method = RequestMethod.POST)
    @ResponseBody
    public void autoAssignRelations() throws IOException {
        systemFacade.autoAssignRelations();
    }

    @RequestMapping(value = "/posibilities", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<ParticipantWithPosibilities> allPosibilities() {
        return systemFacade.allPosibilities();
    }

    @RequestMapping(value = "/inmutables", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<FriendRelation> allInmutableRelations() {
        return systemFacade.allInmutableRelations();
    }

    @RequestMapping(value = "/update/{giverId}/{newReceiverId}", method = RequestMethod.PUT)
    @ResponseBody
    public void updateRelation(@PathVariable Long giverId, @PathVariable Long newReceiverId) throws IOException, MessagingException {
        Worker giver = workerService.retrieveWorker(giverId);
        Worker newReceiver = workerService.retrieveWorker(newReceiverId);
        systemFacade.updateRelation(giver, newReceiver);
    }

    @RequestMapping(value = "/hintsFor/{workerID}", method = RequestMethod.GET)
    @ResponseBody
    public List<Hint> getHintsFor(@PathVariable Long workerID) {
        Worker worker = systemFacade.retrieveAWorker(workerID);
        return systemFacade.hintsFor(worker);
    }

    @RequestMapping(value = "/hintsFrom/{workerID}", method = RequestMethod.GET)
    @ResponseBody
    public List<Hint> getHintsFrom(@PathVariable Long workerID) {
        Worker worker = systemFacade.retrieveAWorker(workerID);
        return systemFacade.hintsFrom(worker);
    }

    @RequestMapping(value = "/hintsLimit", method = RequestMethod.GET)
    @ResponseBody
    public Integer getHintsLimit() {
        return systemFacade.hintsLimit();
    }

    @RequestMapping(value = "/hintsFrom/{workerID}", method = RequestMethod.POST)
    @ResponseBody
    public Hint addHintsFrom(@PathVariable Long workerID, @RequestBody Hint newHint) {
        Worker worker = systemFacade.retrieveAWorker(workerID);
       return systemFacade.addHintFrom(worker,newHint);
    }

    @RequestMapping(value = "/hintsFrom/{workerID}/{hintId}", method = RequestMethod.PUT)
    @ResponseBody
    public void updateHintsFrom(@PathVariable Long workerID, @PathVariable Long hintId, @RequestBody String newHint) {
        Worker worker = systemFacade.retrieveAWorker(workerID);
        systemFacade.updateHintFrom(worker, hintId, new Hint(newHint));
    }

    @RequestMapping(value = "/hintsFrom/{workerID}/{hintId}", method = RequestMethod.DELETE)
    @ResponseBody
    public void removeHintFrom(@PathVariable Long workerID, @PathVariable Long hintId) {
        Worker worker = systemFacade.retrieveAWorker(workerID);
        systemFacade.removeHintFrom(worker, hintId);
    }

    @RequestMapping(value = "/guessFor/{workerID}", method = RequestMethod.PUT)
    @ResponseBody
    public GuessResponse guessGiftGiverFor(@PathVariable Long workerID, @RequestBody String assumedGiftGiverFullName) {
        Worker worker = systemFacade.retrieveAWorker(workerID);
        FriendRelation relationAfterGuess = friendRelationService.guessGiftGiverFor(worker, assumedGiftGiverFullName);
        return new GuessResponse(relationAfterGuess.isGuessed(), relationAfterGuess.getGuessAttempts());
    }

    @RequestMapping(value = "/giftGiverFor/{workerID}", method = RequestMethod.GET)
    @ResponseBody
    public Worker getGiftSenderForWorker(@PathVariable Long workerID) {
        Worker giftReceiver = systemFacade.retrieveAWorker(workerID);
        Optional<Worker> giftSender = friendRelationService.getGiftSenderFor(giftReceiver);
        return giftSender.orElseThrow(CannotGetGiftGiverException::new);
    }

    @ExceptionHandler(CannotGetGiftGiverException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public CannotGetGiftGiverException handleException(CannotGetGiftGiverException e) {
        return e;
    }

    @RequestMapping(value = "/guessFor/{workerID}", method = RequestMethod.GET)
    @ResponseBody
    public GuessResponse getStatusFor(@PathVariable Long workerID) {
        Worker worker = systemFacade.retrieveAWorker(workerID);
        FriendRelation relation = friendRelationService.guessStatusFor(worker);
        return new GuessResponse(relation.isGuessed(), relation.getGuessAttempts());
    }

    @RequestMapping(value = "/guessLimit", method = RequestMethod.GET)
    @ResponseBody
    public Integer getGuessLimit() {
        return systemFacade.guessesLimit();
    }

    @RequestMapping(value = "/giftGiverFor/{workerID}", method = RequestMethod.GET)
    @ResponseBody
    public Worker getGiftSenderForWorker(@PathVariable Long workerID) {
        Worker giftReceiver = systemFacade.retrieveAWorker(workerID);
        Optional<Worker> giftSender = friendRelationService.getGiftSenderFor(giftReceiver);
        return giftSender.orElseThrow(CannotGetGiftGiverException::new);
    }

    @ExceptionHandler(CannotGetGiftGiverException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public CannotGetGiftGiverException handleException(CannotGetGiftGiverException e) {
        return e;
    }

    @RequestMapping(value = "/guessFor/{workerID}", method = RequestMethod.GET)
    @ResponseBody
    public GuessResponse getStatusFor(@PathVariable Long workerID) {
        Worker worker = systemFacade.retrieveAWorker(workerID);
        FriendRelation relation = friendRelationService.guessStatusFor(worker);
        return new GuessResponse(relation.isGuessed(), relation.getRemainingGuessAttempts());
    }

    @RequestMapping(value = "/guessLimit", method = RequestMethod.GET)
    @ResponseBody
    public Integer getGuessLimit() {
        return systemFacade.guessesLimit();
    }

}