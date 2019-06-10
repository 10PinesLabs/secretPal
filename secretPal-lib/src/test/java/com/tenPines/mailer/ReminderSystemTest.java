package com.tenPines.mailer;


import com.tenPines.application.ReminderSystem;
import com.tenPines.application.SecretPalProperties;
import com.tenPines.application.clock.FakeClock;
import com.tenPines.application.service.FriendRelationService;
import com.tenPines.application.service.WorkerService;
import com.tenPines.builder.WorkerBuilder;
import com.tenPines.integration.SpringBaseTest;
import com.tenPines.model.Worker;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


public class ReminderSystemTest extends SpringBaseTest {

    private static final String NON_PARTICIPANT_NICKNAME = "PINE WHO DOESNT PARTICIPATE";
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

    @Value("${mail.recipients}")
    private String birthdayMailRecipient;

    private Worker giftGiver;
    private Worker workerTurningYears;

    @Before
    public void setUp() {
        clock.setTime(LocalDate.now());
    }

    @Test
    public void aBirthdayMailIsSentToEveryoneWhenAParticipantTurnsYears(){
        createFriendRelationWithBirthdayToday();

        reminderSystem.sendHappyBithdayMessages();

        assertThat(postMan.messagesTo(birthdayMailRecipient), hasSize(1));
    }

    @Test
    public void aBirthdayMailIsSentToEveryoneWhenANonParticipantTurnsYears(){
        createNonParticipantTurningYearsToday();

        reminderSystem.sendHappyBithdayMessages();

        assertThat(postMan.messagesTo(birthdayMailRecipient), hasSize(1));
    }

    @Test
    public void aReminderMailIsSentToGiverWhenThereAreTwoWeeksLeftToTheBirthday(){
        createFriendRelationWithBirthdayToday();
        backTheClockTwoWeeks();

        reminderSystem.sendTwoWeeksReminders();

        assertThat(postMan.messagesTo(giftGiver.geteMail()), hasSize(1));
    }


    private void createFriendRelationWithBirthdayToday() {
        giftGiver = workerService.save(new WorkerBuilder().build());
        workerTurningYears = workerService.save(new WorkerBuilder().withBirthDayDate(clock.now()).build());
        friendRelationService.create(giftGiver, workerTurningYears);
    }

    private void createNonParticipantTurningYearsToday() {
        workerService.save(
            new WorkerBuilder()
                    .withBirthDayDate(clock.now())
                    .whoDoesNotWantToParticipate()
                    .withNickname(NON_PARTICIPANT_NICKNAME)
                    .build()
        );
    }

    private void backTheClockTwoWeeks() {
        clock.setTime(clock.now().minusWeeks(secretPalProperties.getReminderWeekPeriod()));
    }


}
