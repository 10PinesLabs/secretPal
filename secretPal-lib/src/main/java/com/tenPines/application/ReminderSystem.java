package com.tenPines.application;

import com.tenPines.application.clock.Clock;
import com.tenPines.application.service.*;
import com.tenPines.builder.HappyBithdayMessageBuilder;
import com.tenPines.mailer.PostOffice;
import com.tenPines.model.FriendRelation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.MonthDay;
import java.util.stream.Stream;

@Service
public class ReminderSystem {
    private final Clock clock;
    private final FriendRelationService friendRelationService;
    private final WorkerService workerService;
    private final SecretPalProperties secretPalProperties;
    private final MailProperties mailProperties;
    private final PostOffice postOffice;
    private final MailerService mailerService;
    private final DefaultGifService defaultGifService;
    private final ReminderBuilderService reminderBuilderService;

    @Autowired
    public ReminderSystem(Clock clock, FriendRelationService friendRelationService, WorkerService workerService, SecretPalProperties secretPalProperties, MailProperties mailProperties, PostOffice postOffice, MailerService mailerService, DefaultGifService defaultGifService, ReminderBuilderService reminderBuilderService) {
        this.clock = clock;
        this.friendRelationService = friendRelationService;
        this.workerService = workerService;
        this.secretPalProperties = secretPalProperties;
        this.mailProperties = mailProperties;
        this.postOffice = postOffice;
        this.mailerService = mailerService;
        this.defaultGifService = defaultGifService;
        this.reminderBuilderService = reminderBuilderService;
    }

    public void sendHappyBithdayMessages() {
        workerService.getAllParticipants().stream()
                .filter(worker ->
                        worker.getBirthday()
                                .equals(
                                        MonthDay.from(clock.now()))
                )
                .forEach(worker -> postOffice.sendMessage(new HappyBithdayMessageBuilder(mailProperties, defaultGifService).buildMessage(worker)));
    }

    public void sendTwoWeeksReminders() {
        relationsWithBirthdayTwoWeeksFromNow()
                .forEach(friendRelation -> {
                    postOffice.sendMessage(reminderBuilderService.buildTwoWeekReminderMessage(friendRelation));
                });
    }

    private Stream<FriendRelation> relationsWithBirthdayTwoWeeksFromNow() {
        return friendRelationService.getAllRelations().stream()
                .filter(friendRelation ->
                        twoWeeksFromNow(friendRelation)
                );
    }

    private boolean twoWeeksFromNow(FriendRelation friendRelation) {
        MonthDay todayPlusTwoWeeks = MonthDay.from(clock.now().plusWeeks(secretPalProperties.getReminderWeekPeriod()));
        MonthDay birthday = friendRelation.getGiftReceiver().getBirthday();
        return birthday.equals(todayPlusTwoWeeks);
    }

    public void sendAllReminders() {
        sendTwoWeeksReminders();
        sendHappyBithdayMessages();
    }
}