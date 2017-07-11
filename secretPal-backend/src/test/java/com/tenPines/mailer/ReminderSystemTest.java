package com.tenPines.mailer;


import com.tenPines.application.ReminderSystem;
import com.tenPines.application.SecretPalProperties;
import com.tenPines.application.clock.FakeClock;
import com.tenPines.application.service.FriendRelationService;
import com.tenPines.application.service.WorkerService;
import com.tenPines.builder.WorkerBuilder;
import com.tenPines.integration.SpringBaseTest;
import com.tenPines.model.Worker;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class ReminderSystemTest extends SpringBaseTest {

    @Autowired
    private WorkerService workerService;
    @Autowired
    private ReminderSystem reminderSystem;
    @Autowired
    private FakeClock clock;
    @Autowired
    private InMemoryPostMan postMan;
    @Autowired
    private SecretPalProperties secretPalProperties;
    @Autowired
    private FriendRelationService friendRelationService;
    private Worker friendWorker;
    private Worker birthdayWorker;


    private void setUp(LocalDate today, LocalDate birthday) {
        clock.setTime(today);

        friendWorker = workerService.save(new WorkerBuilder().build());
        birthdayWorker = workerService.save(new WorkerBuilder().withBirthDayDate(birthday).build());

        friendRelationService.create(friendWorker, birthdayWorker);
    }


    @Test
    public void When_A_Workers_Birthday_Should_Mail_Him(){
        setUp(LocalDate.now(), LocalDate.now());

        reminderSystem.sendHappyBithdayMessages();
        assertThat(postMan.messagesTo(birthdayWorker.geteMail()), empty());
    }

    @Test
    public void When_aproach_the_birthday_of_friendWorker(){
        setUp(LocalDate.now().plusDays(secretPalProperties.getReminderWeekPeriod()), LocalDate.now());

        reminderSystem.sendTwoWeeksReminders();
        assertThat(postMan.messagesTo(friendWorker.geteMail()), empty());
    }


}
