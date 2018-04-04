package com.tenPines.application;

import com.tenPines.application.clock.Clock;
import com.tenPines.application.service.FriendRelationService;
import com.tenPines.application.service.MailerService;
import com.tenPines.application.service.WorkerService;
import com.tenPines.builder.HappyBithdayMessageBuilder;
import com.tenPines.builder.ReminderBuilder;
import com.tenPines.builder.ReminderMonthsBirthdayAproachBuilder;
import com.tenPines.builder.ReminderWeeksBirthdayAproachBuilder;
import com.tenPines.mailer.PostOffice;
import com.tenPines.model.FriendRelation;
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

    @Autowired
    public ReminderSystem(Clock clock, FriendRelationService friendRelationService, WorkerService workerService, SecretPalProperties secretPalProperties, MailProperties mailProperties, PostOffice postOffice, MailerService mailerService) {
        this.clock = clock;
        this.friendRelationService = friendRelationService;
        this.workerService = workerService;
        this.secretPalProperties = secretPalProperties;
        this.mailProperties = mailProperties;
        this.postOffice = postOffice;
        this.mailerService = mailerService;
    }


//    @Scheduled(fixedDelay = 86400000)
    public void sendHappyBithdayMessages() {
        workerService.getAllParticipants().stream()
                .filter(worker ->
                        MonthDay.from(worker.getDateOfBirth())
                                .equals(
                                        MonthDay.from(clock.now()))
                )
                .forEach(worker -> postOffice.sendMessage(new HappyBithdayMessageBuilder(mailProperties).buildMessage(worker)));

    }
    
//    @Scheduled(fixedDelay = 86400000) //86400000 = 1 dia
    public void sendTwoWeeksReminders() {
        relationsWithBirthdayTwoWeeksFromNow()
                .forEach(friendRelation -> {
                    tryToSendReminderMessage(friendRelation, new ReminderWeeksBirthdayAproachBuilder(mailerService));
                });
    }

//    @Scheduled(fixedDelay = 86400000) //86400000 = 1 dia
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
        MonthDay birthday = MonthDay.from(friendRelation.getGiftReceiver().getDateOfBirth());
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
        MonthDay birthday = MonthDay.from(friendRelation.getGiftReceiver().getDateOfBirth());
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
        sendHappyBithdayMessages();
    }
}
