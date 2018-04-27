package com.tenPines.mailer;

import com.tenPines.application.service.MailerService;
import com.tenPines.application.service.ReminderBuilderService;
import com.tenPines.application.service.ReplacerService;
import com.tenPines.builder.ReminderBuilder;
import com.tenPines.builder.WorkerBuilder;
import com.tenPines.integration.SpringBaseTest;
import com.tenPines.model.FriendRelation;
import com.tenPines.model.Message;
import com.tenPines.model.Worker;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.Month;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class ReminderBuilderTest extends SpringBaseTest {

    @Autowired
    private MailerService mailerService;
    @Autowired
    private ReminderBuilderService reminderBuilderService;
    @Autowired
    private ReplacerService replacerService;

    private Worker birthdayWorker;
    private Worker workerGiver;
    private FriendRelation friendRelation;
    private ReminderMonthsBirthdayAproachBuilder reminderMonthsBuilder;
    private ReminderWeeksBirthdayAproachBuilder reminderWeeksBuilder;

    @Before
    public void setUp() {
        birthdayWorker = new WorkerBuilder().withFullName("Cacho Castania")
                .withBirthDayDate(LocalDate.of(1950, Month.DECEMBER, 10)).build();
        workerGiver = new WorkerBuilder().build();
        friendRelation = new FriendRelation(workerGiver, birthdayWorker);
    }

    @Test
    public void whenOneOfTheRequiredRemainderEmailTemplatesDoesntExistsAnExceptionShouldBeRaised() {
        String inexistentTemplateName = "inexistent";
        ReminderBuilder reminderBuilder = new ReminderBuilder(inexistentTemplateName, replacerService);
        try {
            reminderBuilder.buildMessage(friendRelation);
            fail("The exception was not raised");
        } catch (RuntimeException e) {
            assertThat(e.getMessage(), is("resource mail-templates/" + inexistentTemplateName + "-subject.txt not found."));
        }
    }

    @Test
    public void whenEmailTemplatesExistThenEmailSubjectPlainTextAndHtmlBodyShouldBeAsTemplate() {
        String templateName = "test_file";
        ReminderBuilder reminderBuilder = new ReminderBuilder(templateName, replacerService);

        Message message = reminderBuilder.buildMessage(friendRelation);

        assertThat(message.getSubject(), is("Subject Test Content"));
        assertThat(message.getPlainTextBody(), is("Plain Text Test Content"));
        assertThat(message.getHtmlBody(), is("Html Text Test Content"));
    }

    @Test
    public void whenTheEmailTemplateHasFullNameVariableThenShouldReplaceThisForReceiverFullName() {
        Message message = reminderBuilderService.buildTwoWeekReminderMessage(friendRelation);
        String expectedFullName = "Cacho Castania";

        assertThat(message.getPlainTextBody(), containsString(expectedFullName));
    }

    @Test
    public void whenTheEmailTemplateHasDateOfBirthVariableThenShouldReplaceThisForReceiverDateOfBirth() {
        Message message = reminderBuilderService.buildTwoWeekReminderMessage(friendRelation);
        String expectedDateOfBirth = "10 de diciembre";

        assertThat(message.getPlainTextBody(), containsString(expectedDateOfBirth));
    }

    @Test
    public void whenTheEmailTemplateHasAVariableThatCannotBeReplacedAnErrorShouldBeRaised() {
        String templateName = "inexistent_variable";
        ReminderBuilder reminderBuilder = new ReminderBuilder(templateName, replacerService);

        try {
            reminderBuilder.buildMessage(friendRelation);
            fail("The exception was not raised");
        } catch (RuntimeException e) {
            assertThat(e.getMessage(), is("${inexistentVariable} is not a valid variable"));
        }
    }

}
