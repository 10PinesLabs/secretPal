package com.tenPines.restAPI;

import com.tenPines.application.service.WorkerService;
import com.tenPines.model.FriendRelation;
//import com.tenPines.model.SecretPalEvent;
import com.tenPines.model.Worker;
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

import static com.tenPines.application.service.validation.rule.NotCircularRuleValidator.friendRelationService;

@Controller
@RequestMapping("/api/friendRelation")
public class FriendRelationController {

    @Autowired
    private WorkerService workerService;

    @RequestMapping(value = "/", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<FriendRelation> workersWithFriends() {
        return friendRelationService.getAllRelations();
    }


    @RequestMapping(value = "/posibleFriend/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<Worker> posiblesFriends(@PathVariable Long id) {
        Worker workerTo = workerService.retriveWorkerOrThrow(id);
        return friendRelationService.getAvailablesRelationsTo(workerTo);


    }
    @RequestMapping(value = "/", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public void createRelation(@RequestBody @Valid List<FriendRelation> friendRelations) throws IOException, MessagingException {

        friendRelationService.deleteAllRelations();
        for (FriendRelation friendRelation : friendRelations) {
            friendRelationService.create(friendRelation.getGiftGiver(), friendRelation.getGiftReceiver());
        }
    }

    @RequestMapping(value = "/{from}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteRelation(@PathVariable Long from){
        Worker giver = workerService.retriveWorkerOrThrow(from);
        friendRelationService.deleteByGiftGiver(giver);
    }

    @RequestMapping(value = "/friend/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Worker retrieveGiftee(@PathVariable("id") Long id) {
        Worker participant = workerService.retriveWorkerOrThrow(id);
        return friendRelationService.retrieveAssignedFriendFor(participant);
    }

    @RequestMapping(value = "/autoAssign", method = RequestMethod.POST)
    @ResponseBody
    public void autoAssignRelations() throws IOException {
        friendRelationService.autoAssignRelations();
    }

    @RequestMapping(value = "/posibilities", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<ParticipantWithPosibilities> allPosibilities() {
        return friendRelationService.allPosibilities();
    }

    @RequestMapping(value = "/inmutables", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<FriendRelation> allInmutableRelations() {
        return friendRelationService.allInmutableRelations();
    }

    @RequestMapping(value = "/update/{giverId}/{newReceiverId}", method = RequestMethod.PUT)
    @ResponseBody
    public void updateRelation(@PathVariable Long giverId,@PathVariable Long newReceiverId) throws IOException, MessagingException {
        Worker giver = workerService.retriveWorkerOrThrow(giverId);
        Worker newReceiver = workerService.retriveWorkerOrThrow(newReceiverId);
        friendRelationService.updateRelation(giver, newReceiver);
    }
}