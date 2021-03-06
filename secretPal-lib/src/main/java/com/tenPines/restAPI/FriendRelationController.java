package com.tenPines.restAPI;

import com.tenPines.application.SystemPalFacade;
import com.tenPines.application.service.FriendRelationService;
import com.tenPines.application.service.WorkerService;
import com.tenPines.model.FriendRelation;
import com.tenPines.model.Hint;
import com.tenPines.model.Worker;
import com.tenPines.restAPI.utils.GuessResponseForFrontend;
import com.tenPines.restAPI.utils.PossibleRelationForFrontEnd;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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

    @RequestMapping(value = "/posibilities", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<PossibleRelationForFrontEnd> allPosibilities() {
        List<PossibleRelationForFrontEnd> possibleRelationsForFrontEnd = systemFacade.allReceiversWithPosibilities();
        possibleRelationsForFrontEnd.sort(PossibleRelationForFrontEnd::orderByBirthdayDate);
        return possibleRelationsForFrontEnd;
    }

    @RequestMapping(value = "/inmutables", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<FriendRelation> allImmutableRelations() {
        return systemFacade.allImmutableRelations();
    }

    @RequestMapping(value = "{id}/makeImmutable", method = RequestMethod.PUT)
    @ResponseBody
    public void makeRelationImmutable(@PathVariable("id") Long relationId) {
        FriendRelation relation = friendRelationService.retrieveRelation(relationId);
        friendRelationService.makeImmutable(relation);
        systemFacade.sendConfirmationEmailFor(relation);
    }

    @RequestMapping(value = "/update/{giverId}/{newReceiverId}", method = RequestMethod.PUT)
    @ResponseBody
    public void updateRelation(@PathVariable Long giverId, @PathVariable Long newReceiverId) {
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
    public GuessResponseForFrontend guessGiftGiverFor(@PathVariable Long workerID, @RequestBody String assumedGiftGiverFullName) {
        Worker worker = systemFacade.retrieveAWorker(workerID);
        FriendRelation relationAfterGuess = friendRelationService.guessGiftGiverFor(worker, assumedGiftGiverFullName);
        return new GuessResponseForFrontend(relationAfterGuess.isGuessed(), relationAfterGuess.getGuessAttempts(),friendRelationService.getGiftSenderFor(worker));
    }

    @ExceptionHandler(CannotGetGiftGiverException.class)
    @ResponseBody
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public CannotGetGiftGiverException handleException(CannotGetGiftGiverException e) {
        return e;
    }

    @RequestMapping(value = "/guessFor/{workerID}", method = RequestMethod.GET)
    @ResponseBody
    public GuessResponseForFrontend getStatusFor(@PathVariable Long workerID) {
        Worker worker = systemFacade.retrieveAWorker(workerID);
        return systemFacade.guessStatusFor(worker);

    }

    @RequestMapping(value = "/guessLimit", method = RequestMethod.GET)
    @ResponseBody
    public Integer getGuessLimit() {
        return systemFacade.guessesLimit();
    }

}