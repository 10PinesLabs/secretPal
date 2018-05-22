package com.tenPines.application;

import com.tenPines.application.clock.Clock;
import com.tenPines.application.service.DefaultGifService;
import com.tenPines.application.service.FriendRelationService;
import com.tenPines.application.service.MailerService;
import com.tenPines.application.service.WorkerService;
import com.tenPines.builder.HappyBithdayMessageBuilder;
import com.tenPines.builder.ReminderBuilder;
import com.tenPines.builder.ReminderMonthsBirthdayAproachBuilder;
import com.tenPines.builder.ReminderWeeksBirthdayAproachBuilder;
import com.tenPines.mailer.PostOffice;
import com.tenPines.model.FriendRelation;
import com.tenPines.model.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

    @Autowired
    public ReminderSystem(Clock clock, FriendRelationService friendRelationService, WorkerService workerService, SecretPalProperties secretPalProperties, MailProperties mailProperties, PostOffice postOffice, MailerService mailerService, DefaultGifService defaultGifService) {
        this.clock = clock;
        this.friendRelationService = friendRelationService;
        this.workerService = workerService;
        this.secretPalProperties = secretPalProperties;
        this.mailProperties = mailProperties;
        this.postOffice = postOffice;
        this.mailerService = mailerService;
        this.defaultGifService = defaultGifService;
    }

    public void sendHappyBirthdayMessages() {
        workerService.getAllGiverParticipants().stream()
                .filter(worker ->
                        birthdayToday(worker)
                        && worker.wantsToReceiveBirthdayMessage()
                )
                .forEach(worker -> postOffice.sendMessage(new HappyBithdayMessageBuilder(mailProperties, defaultGifService).buildMessage(worker)));

    }

    private boolean birthdayToday(Worker worker) {
        return worker.getBirthday().equals(MonthDay.from(clock.now()));
    }

    public void sendTwoWeeksReminders() {
        relationsWithBirthdayTwoWeeksFromNow()
                .forEach(friendRelation -> {
                    tryToSendReminderMessage(friendRelation, new ReminderWeeksBirthdayAproachBuilder(mailerService));
                });
    }

    public void sendTwoMonthsReminders() {
        relationsWithBirthdayTwoMonthsFromNow()
            .forEach(friendRelation -> {
                tryToSendReminderMessage(friendRelation, new ReminderMonthsBirthdayAproachBuilder(mailerService));
            });
    }

    private Stream<FriendRelation> relationsWithBirthdayTwoMonthsFromNow() {
        return friendRelationService.getAllRelations().stream()
            .filter(friendRelation ->
                    twoMonthsFromNow(friendRelation)
            );
    }

    private boolean twoMonthsFromNow(FriendRelation friendRelation) {
        MonthDay todayPlusTwoMonths = MonthDay.from(clock.now().plusMonths(secretPalProperties.getReminderMonthPeriod()));
        MonthDay birthday = friendRelation.getGiftReceiver().getBirthday();
        return birthday.equals(todayPlusTwoMonths);
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

    private void tryToSendReminderMessage(FriendRelation friendRelation, ReminderBuilder reminderBuilder) {
        try {
            postOffice.sendMessage(
                    reminderBuilder.buildMessage(friendRelation)
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendAllReminders() {
        sendTwoMonthsReminders();
        sendTwoWeeksReminders();
        sendHappyBirthdayMessages();
    }
}
