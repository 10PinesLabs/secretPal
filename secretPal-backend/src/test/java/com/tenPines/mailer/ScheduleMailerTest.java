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
import java.time.Month;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ScheduleMailerTest extends SpringBaseTest {
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
    public void When_A_Worker_Has_A_Friends_Birthday_A_Week_From_Now_The_System_Should_Mail_Him(){
        setUp(LocalDate.of(2000, Month.APRIL, 3), LocalDate.of(2000, Month.APRIL, 10));

        reminderSystem.sendRemindersTheLastBirthday();

        assertThat(postMan.messagesTo(friendWorker.geteMail()), hasSize(1));
        assertThat(postMan.messagesTo(friendWorker.geteMail()), contains(hasProperty("body",
                allOf(
                        containsString(birthdayWorker.getFullName())
                        ))));
    }

    @Test
    public void When_A_Worker_Has_A_Friends_Birthday_After_Today_No_Mail_Should_be_Sent() {
        setUp(LocalDate.of(2000, Month.APRIL, 9), LocalDate.of(1900, Month.JANUARY, 10));

        reminderSystem.sendRemindersTheLastBirthday();

        assertThat(postMan.messagesTo(friendWorker.geteMail()), empty());
    }

    @Test
    public void When_A_Worker_Has_A_Friends_Birthday_Two_Months_From_Now_The_System_Should_Mail_Him(){
        setUp(LocalDate.of(2000, Month.AUGUST, 1), LocalDate.of(2000, Month.OCTOBER, 1));

        reminderSystem.sendTwoMonthsReminders();

        assertThat(postMan.messagesTo(friendWorker.geteMail()), hasSize(1));
        assertThat(postMan.messagesTo(friendWorker.geteMail()), contains(hasProperty("body",
                allOf(
                        containsString(birthdayWorker.getFullName())
                ))));
    }

}