package com.tenPines.mailer;

import com.tenPines.application.service.MailerService;
import com.tenPines.builder.ReminderMonthsBirthdayAproachBuilder;
import com.tenPines.builder.ReminderWeeksBirthdayAproachBuilder;
import com.tenPines.builder.WorkerBuilder;
import com.tenPines.integration.SpringBaseTest;
import com.tenPines.model.EmailTemplate;
import com.tenPines.model.FriendRelation;
import com.tenPines.model.Message;
import com.tenPines.model.Worker;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class ReminderBuilderTest extends SpringBaseTest {

    @Autowired
    private MailerService mailerService;

    private Worker birthdayWorker;
    private Worker workerGiver;
    private FriendRelation friendRelation;
    private ReminderMonthsBirthdayAproachBuilder reminderMonthsBuilder;
    private ReminderWeeksBirthdayAproachBuilder reminderWeeksBuilder;


    @Before
    public void setUp() {
        birthdayWorker = birthdayWorker = new WorkerBuilder().withFullName("Cacho Castania")
                .withBirthDayDate(LocalDate.of(1950, Month.DECEMBER, 10)).build();
        workerGiver = new WorkerBuilder().build();
        friendRelation = new FriendRelation(workerGiver, birthdayWorker);

        reminderMonthsBuilder = new ReminderMonthsBirthdayAproachBuilder(mailerService);
        reminderWeeksBuilder = new ReminderWeeksBirthdayAproachBuilder(mailerService);

        mailerService.deleteTemplate();
    }

    private EmailTemplate eMailTemplate(String body) {
        EmailTemplate template = new EmailTemplate();
        template.setBodyText(body);
        return template;
    }

    @Test
    public void whenDoesntExistsEmailTemplateThenTwoMonthsEmailBodyShouldBeAsDefault() throws IOException {
        Message message = reminderMonthsBuilder.buildMessage(friendRelation);
        String expectedBody = reminderMonthsBuilder.defaultBody(friendRelation.getGiftReceiver());

        assertThat(message.getBody(), is(expectedBody));
    }

    @Test
    public void whenDoesntExistsEmailTemplateThenTwoWeeksEmailBodyShouldBeAsDefault() throws IOException {
        Message message = reminderWeeksBuilder.buildMessage(friendRelation);
        String expectedBody = reminderWeeksBuilder.defaultBody(friendRelation.getGiftReceiver());

        assertThat(message.getBody(), is(expectedBody));
    }

    @Test
    public void whenExistsEmailTemplateThenEmailBodyShouldBeAsTemplate() throws IOException {
        mailerService.setEmailTemplate(eMailTemplate("Body template!"));

        Message message = reminderMonthsBuilder.buildMessage(friendRelation);
        String expectedBody = "Body template!";

        assertThat(message.getBody(), is(expectedBody));
    }

    @Test
    public void whenTheEmailTemplateHasFullNameVariableThenShouldReplaceThisForReceiverFullName() throws IOException {
        mailerService.setEmailTemplate(eMailTemplate("Tu pino asignado es ${receiver.fullName}!"));

        Message message = reminderMonthsBuilder.buildMessage(friendRelation);
        String expectedBody = "Tu pino asignado es Cacho Castania!";

        assertThat(message.getBody(), is(expectedBody));
    }

    @Test
    public void whenTheEmailTemplateHasDateOfBirthVariableThenShouldReplaceThisForReceiverDateOfBirth() throws IOException {
        mailerService.setEmailTemplate(eMailTemplate("Tu pino asignado cumple el ${receiver.dateOfBirth}!"));

        Message message = reminderMonthsBuilder.buildMessage(friendRelation);
        String expectedBody = "Tu pino asignado cumple el 10 de diciembre!";

        assertThat(message.getBody(), is(expectedBody));
    }

}
