package com.tenPines.restAPI;

import com.tenPines.application.SystemPalFacade;
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

@Controller
@RequestMapping("/api/friendRelation")
public class FriendRelationController {


    @Autowired
    private SystemPalFacade systemFacade;

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
    public void createRelation(@RequestBody @Valid List<FriendRelation> friendRelations) throws IOException, MessagingException {
        
        systemFacade.deleteAllRelations();
        for (FriendRelation friendRelation : friendRelations) {
            systemFacade.createRelation(friendRelation.getGiftGiver(), friendRelation.getGiftReceiver());
        }
    }

    @RequestMapping(value = "/{from}", method = RequestMethod.DELETE)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteRelation(@PathVariable Long from){
        Worker giver = workerService.retriveWorker(from);
        systemFacade.deleteRelation(giver);
    }

    @RequestMapping(value = "/friend/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Worker retrieveGiftee(@PathVariable("id") Long id) {
        return systemFacade.retrieveAssignedFriendFor(id);
    }

    @RequestMapping(value = "/autoAssign", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public List<FriendRelation> autoAssignRelations() throws IOException {
        systemFacade.autoAssignRelations();
        return systemFacade.getAllRelations();
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
    public void updateRelation(@PathVariable Long giverId,@PathVariable Long newReceiverId) throws IOException, MessagingException {
        Worker giver = workerService.retriveWorker(giverId);
        Worker newReceiver = workerService.retriveWorker(newReceiverId);
        systemFacade.updateRelation(giver, newReceiver);
    }
}