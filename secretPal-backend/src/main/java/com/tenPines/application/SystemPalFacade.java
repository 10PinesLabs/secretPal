package com.tenPines.application;

import com.tenPines.application.clock.Clock;
import com.tenPines.application.service.*;
import com.tenPines.application.service.validation.rule.AssignationRule;
import com.tenPines.application.service.validation.rule.CustomParticipantRule;
import com.tenPines.application.service.validation.rule.NotCircularRelationRule;
import com.tenPines.application.service.validation.rule.NotTooCloseBirthdaysRule;
import com.tenPines.mailer.UnsentMessage;
import com.tenPines.model.*;
import com.tenPines.restAPI.SecurityToken;
import com.tenPines.restAPI.utils.ParticipantWithPosibilities;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class SystemPalFacade {

    private final FriendRelationService friendRelationService;
    private final WorkerService workerService;
    private final GiftDefaultService giftDefaultService;
    private final WishListService wishListService;
    private final MailerService mailerService;
    private final CustomParticipantRuleService customParticipantRuleService;
    private Long reminderDayPeriod;
    private Clock clock;
    private final ReminderSystem reminderSystem;

    public Worker retrieveAssignedFriendFor(Long Idparticipant) {
        Worker participant = workerService.retriveWorker(Idparticipant);
        return friendRelationService.retrieveAssignedFriendFor(participant);
    }

    public FriendRelation createRelation(Worker giftGiver, Worker giftReceiver) {
        return friendRelationService.create(giftGiver, giftReceiver);
    }

    public void deleteRelation(Worker giver) {
        friendRelationService.deleteByGiftGiver(giver);
    }

    public List<DefaultGift> retrieveAllGiftsDefaults() {
        List<DefaultGift> defaultGifts = giftDefaultService.getAll();
        if (defaultGifts.isEmpty()) {

            defaultGifts.add(DefaultGift.createGiftDfault("Nada", "$0"));
        }
        return defaultGifts;
    }

    public void addGiftDefaults(DefaultGift defaultGift) {
        giftDefaultService.addGift(defaultGift);

    }

    public List<Wish> retrieveAllWishes() {
        return wishListService.retrieveAllWishes();
    }

    public Worker retrieveAWorker(Long workerID) {
        return workerService.retriveWorker(workerID);
    }

    public List<Wish> retrievallWishesForWorker(Worker worker) {
        return wishListService.retrieveByWorker(worker);
    }

    public Wish saveWish(Wish wish) {

        return wishListService.saveWish(wish);
    }

    public Wish retrieveAWish(Long id) {
        return wishListService.retrieveAWish(id);
    }

    public void updateWish(Wish wish) {
        wishListService.updateWish(wish);
    }

    public void deleteAWish(Wish wish) {
        wishListService.deleteAWish(wish);
    }

    public SystemPalFacade(FriendRelationService friendRelationService, WorkerService workerService, GiftDefaultService giftDefaultService, WishListService wishListService, MailerService mailerService, CustomParticipantRuleService customParticipantRuleService, ReminderSystem reminderSystem) {
        this.reminderSystem = reminderSystem;
        setReminderDayPeriod(7L);
        this.friendRelationService = friendRelationService;
        this.workerService = workerService;
        this.giftDefaultService = giftDefaultService;
        this.wishListService = wishListService;
        this.mailerService = mailerService;
        this.customParticipantRuleService = customParticipantRuleService;
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

    public DefaultGift retrieveTheLastDefaultGift() {
        return retrieveAllGiftsDefaults().get(0);
    }

    public void deleteAllRelations() {
        friendRelationService.deleteAllRelations();
    }

    public List<Worker> getPossibleFriendsTo(Long id) {
        Worker workerTo = workerService.retriveWorker(id);
        return friendRelationService.getAvailablesRelationsTo(workerTo);
    }

    public void editWorker(Worker workerEdited) throws Exception {
        workerService.save(workerEdited);
    }

    public EmailTemplate getEMailTemplate() throws IOException {
        return mailerService.getEMailTemplate();

    }

    public EmailTemplate setEmailTemplate(EmailTemplate modifiedMail) throws IOException {
        return mailerService.setEmailTemplate(modifiedMail);
    }

    public List<UnsentMessage> retrieveAllFailedMails() {
        return mailerService.retrieveAllFailedMails();
    }

    public void resendMessageFailure(UnsentMessage unsentMessage) {

        mailerService.resendMessageFailure(unsentMessage);
    }

    public void createRule(Worker workerFrom, Worker workerTo, Boolean isActive) {
        customParticipantRuleService.create(workerFrom, workerTo, isActive);
    }

    public void autoAssignRelations() {
        friendRelationService.autoAssignRelations();
    }

    public void deleteRule(Long id) {
        customParticipantRuleService.delete(id);
    }

    public List<CustomParticipantRule> getAllCustomRules() {
        return customParticipantRuleService.getAllCustomRules();
    }
    public List<FriendRelation> getAllRelations() {
        return friendRelationService.getAllRelations();
    }

    public NotCircularRelationRule getCircularRule() {
        return customParticipantRuleService.getCircularRule();
    }

    public NotTooCloseBirthdaysRule getNotTooCloseBirthdayRule() {
        return customParticipantRuleService.getNotTooCloseBirthdayRule();
    }

    public void updateCircularRule(NotCircularRelationRule rule){
        customParticipantRuleService.updateCircularRule(rule);
    }
    public void updateBirthdayRule(NotTooCloseBirthdaysRule rule) {
        customParticipantRuleService.updateRuleBirthday(rule);
    }

    public List<ParticipantWithPosibilities> allPosibilities() {
        return friendRelationService.allPosibilities();
    }

    public List<FriendRelation> allInmutableRelations() {
        return friendRelationService.allInmutableRelations();
    }

    public void updateRelation(Worker giver, Worker newReceiver) {
        friendRelationService.updateRelation(giver, newReceiver);
    }

    public void sendAllTodayReminders() {
        reminderSystem.sendAllReminders();
    }

    public List<String> hintsFor(Worker worker) {
        return friendRelationService.retrieveHintsGivenTo(worker);
    }

    public List<String> hintsFrom(Worker worker) {
        return friendRelationService.retrieveHintsFrom(worker);

    }
}