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
import org.apache.commons.lang.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.Month;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;


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
        birthdayWorker = new WorkerBuilder().withFullName("Cacho Castania")
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

    private String plainTextFromHtml(String htmlText) {
        String plainText = String.join(" ",
                StringUtils.substringsBetween(htmlText, "<p>", "</p>"));
        if(plainText == null){
            throw new RuntimeException("The html message is missing <p> and </p> tags.");
        }
        return plainText;
    }

    @Test
    public void whenDoesntExistsEmailTemplateThenTwoMonthsEmailBodyShouldBeAsDefault() {
        Message message = reminderMonthsBuilder.buildMessage(friendRelation);
        String expectedBody = plainTextFromHtml(reminderMonthsBuilder.defaultHtmlBody(friendRelation.getGiftReceiver()));

        assertThat(message.getPlainTextBody(), is(expectedBody));
    }

    @Test
    public void whenDoesntExistsEmailTemplateThenTwoWeeksEmailBodyShouldBeAsDefault() {
        Message message = reminderWeeksBuilder.buildMessage(friendRelation);
        String expectedBody = plainTextFromHtml(reminderWeeksBuilder.defaultHtmlBody(friendRelation.getGiftReceiver()));

        assertThat(message.getPlainTextBody(), is(expectedBody));
    }

    @Test
    public void whenExistsEmailTemplateButItDoesNotHaveHtmlParagraphTagsTheMessageCannotBeBuilt() {
        mailerService.setEmailTemplate(eMailTemplate("Body template!"));

        try {
            reminderMonthsBuilder.buildMessage(friendRelation);;
            fail("The exception was not raised");
        } catch (RuntimeException e) {
            assertThat(e.getMessage(), is("The html message is missing <p> and </p>"));
        }
    }

    @Test
    public void whenExistsEmailTemplateThenEmailBodyShouldBeAsTemplate() {
        mailerService.setEmailTemplate(eMailTemplate("<p>Body template!</p>"));

        Message message = reminderMonthsBuilder.buildMessage(friendRelation);
        String expectedBody = "Body template!";

        assertThat(message.getPlainTextBody(), is(expectedBody));
    }

    @Test
    public void whenTheEmailTemplateHasFullNameVariableThenShouldReplaceThisForReceiverFullName() {
        mailerService.setEmailTemplate(eMailTemplate("<p>Tu pino asignado es ${receiver.fullName}!</p>"));

        Message message = reminderMonthsBuilder.buildMessage(friendRelation);
        String expectedBody = "Tu pino asignado es Cacho Castania!";

        assertThat(message.getPlainTextBody(), is(expectedBody));
    }

    @Test
    public void whenTheEmailTemplateHasDateOfBirthVariableThenShouldReplaceThisForReceiverDateOfBirth() {
        mailerService.setEmailTemplate(eMailTemplate("<p>Tu pino asignado cumple el ${receiver.dateOfBirth}!</p>"));

        Message message = reminderMonthsBuilder.buildMessage(friendRelation);
        String expectedBody = "Tu pino asignado cumple el 10 de diciembre!";

        assertThat(message.getPlainTextBody(), is(expectedBody));
    }

    @Test
    public void whenTheEmailTemplateHasAVariableThatCannotBeReplacedAnErrorShouldBeRaised() {
        mailerService.setEmailTemplate(eMailTemplate("<p>${inexistent}!</p>"));

        try {
            reminderMonthsBuilder.buildMessage(friendRelation);
            fail("The exception was not raised");
        } catch (RuntimeException e) {
            assertThat(e.getMessage(), is("${inexistent} is not a valid variable"));
        }
    }

}
