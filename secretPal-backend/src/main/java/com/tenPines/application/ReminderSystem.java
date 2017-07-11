package com.tenPines.application;

import com.tenPines.application.clock.Clock;
import com.tenPines.application.service.FriendRelationService;
import com.tenPines.application.service.MailerService;
import com.tenPines.application.service.WorkerService;
import com.tenPines.builder.HappyBithdayMessageBuilder;
import com.tenPines.builder.ReminderMonthsBirthdayAproachBuilder;
import com.tenPines.builder.ReminderWeeksBirthdayAproachBuilder;
import com.tenPines.mailer.PostOffice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.MonthDay;

@Service
public class ReminderSystem {
    private final Clock clock;
    private final FriendRelationService friendRelationService;
    private final WorkerService workerService;
    private final SecretPalProperties secretPalProperties;
    private final PostOffice postOffice;
    private final MailerService mailerService;

    @Autowired
    public ReminderSystem(Clock clock, FriendRelationService friendRelationService, WorkerService workerService, SecretPalProperties secretPalProperties, PostOffice postOffice, MailerService mailerService) {
        this.clock = clock;
        this.friendRelationService = friendRelationService;
        this.workerService = workerService;
        this.secretPalProperties = secretPalProperties;
        this.postOffice = postOffice;
        this.mailerService = mailerService;
    }


    @Scheduled(fixedDelay = 86400000)
    public void sendHappyBithdayMessages() {
        workerService.getAllParticipants().stream()
                .filter(worker ->
                        MonthDay.from(worker.getDateOfBirth())
                                .equals(
                                        MonthDay.from(clock.now()))
                )
                .forEach(worker -> postOffice.sendMessage(new HappyBithdayMessageBuilder().buildMesage(worker)));

    }
    
    @Scheduled(fixedDelay = 86400000) //86400000 = 1 dia
    public void sendTwoWeeksReminders() {
        friendRelationService.getAllRelations().stream()
                .filter(friendRelation ->
                        MonthDay.from(friendRelation.getGiftReceiver().getDateOfBirth())
                                .equals(
                                        MonthDay.from(clock.now().plusWeeks(secretPalProperties.getReminderWeekPeriod())))
                )
                .forEach(friendRelation -> {
                    try {
                        postOffice.sendMessage(
                                new ReminderWeeksBirthdayAproachBuilder(mailerService).buildMessage(friendRelation)
                        );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    @Scheduled(fixedDelay = 86400000) //86400000 = 1 dia
    public void sendTwoMonthsReminders() {
        friendRelationService.getAllRelations().stream()
                .filter(friendRelation ->
                        MonthDay.from(friendRelation.getGiftReceiver().getDateOfBirth())
                                .equals(
                                        MonthDay.from(clock.now().plusMonths(secretPalProperties.getReminderMonthPeriod())))
                )
                .forEach(friendRelation -> {
                    try {
                        postOffice.sendMessage(
                                new ReminderMonthsBirthdayAproachBuilder(mailerService).buildMessage(friendRelation)
                        );
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

}
