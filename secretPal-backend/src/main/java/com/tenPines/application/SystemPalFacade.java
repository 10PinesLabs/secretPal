package com.tenPines.application;

import com.tenPines.application.clock.Clock;
import com.tenPines.application.service.FriendRelationService;
import com.tenPines.application.service.GiftDefaultService;
import com.tenPines.application.service.WishlistService;
import com.tenPines.application.service.WorkerService;
import com.tenPines.model.*;
import com.tenPines.restAPI.SecurityToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SystemPalFacade {

    @Autowired
    FriendRelationService friendRelationService;

    @Autowired
    WorkerService workerService;

    @Autowired
    GiftDefaultService giftDefaultService;

    @Autowired
    WishlistService wishlistService;

    @Autowired
    SecurityGuard securityGuard;

    @Autowired
    RegisterService registerService;

    private Long reminderDayPeriod;
    private Clock clock;

    public Worker retrieveAssignedFriendFor(Long Idparticipant) {
        Worker participant = workerService.retriveWorker(Idparticipant);
        return friendRelationService.retrieveAssignedFriendFor(participant);
    }

    public FriendRelation createRelation(Worker giftGiver, Worker giftReceiver) {
        return friendRelationService.create(giftGiver,giftReceiver);
    }

    public void deleteRelation(Long from, Long to) {
        Worker participant = workerService.retriveWorker(to);
        friendRelationService.retrieveAssignedFriendFor(participant);
    }

    public List<GiftDefault> retrieveAllGiftsDefaults() {
        List<GiftDefault> giftDefaults = giftDefaultService.getAll();
        return giftDefaults;
    }

    public void addGiftDefaults(GiftDefault giftDefault) {
        giftDefaultService.addGift(giftDefault);

    }

    public List<Wish> retrieveAllWishes() {
        return wishlistService.retrieveAllWishes();
    }

    public Worker retrieveAWorker(Long workerID) {
        return workerService.retriveWorker(workerID);
    }

    public List<Wish> retrievallWishesForWorker(Worker worker) {
        return wishlistService.retrieveByWorker(worker);
    }

    public Wish saveWish(Wish wish) {

        return wishlistService.saveWish(wish);
    }

    public Wish retrieveAWish(Long id) {
        return wishlistService.retrieveAWish(id);
    }

    public void updateWish(Wish wish) {
        wishlistService.updateWish(wish);
    }

    public void deleteAWish(Wish wish) {
        wishlistService.deleteAWish(wish);
    }

    public Worker retrieveWorkerByEmail(String email) {
        return workerService.retrieveWorkerByEmail(email);

    }
    public SystemPalFacade() {
        setReminderDayPeriod(7L);
    }

    public Long getReminderDayPeriod() {
        return reminderDayPeriod;
    }

    public void setReminderDayPeriod(Long reminderDayPeriod) {
        this.reminderDayPeriod = reminderDayPeriod;
    }

    public void setClock(Clock clock) {
        this.clock = clock;
    }

    public Clock getClock() {
        return clock;
    }

    public void deleteAWorker(Worker worker) {
        workerService.remove(worker);
    }

    public void changeIntention(Worker aWorker) {
        workerService.changeIntention(aWorker);
    }

    public List<Worker> getAllWorkers() {
        return workerService.getAllWorkers();
    }

    public SecurityToken loginWithInternalCredential(Credential aCredential){
        String token = securityGuard.enterWith(aCredential);
        return SecurityToken.createWith(token);
    }

    public void registerUserAndAsociateWithAWorker(NewUser form){
        NewUser newUser = NewUser.createANewUser(form.getUserName(), form.getPassword(), form.getEmail());
        registerService.registerUser(newUser);
    }
}