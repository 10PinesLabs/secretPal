package com.tenPines.mailer;

import com.github.javafaker.Faker;
import com.tenPines.integration.SpringBaseTest;
import com.tenPines.model.Message;
import com.tenPines.persistence.FailedMailsRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.mail.MessagingException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class SMTPPostManTest extends SpringBaseTest {

    @Autowired
    private FailedMailsRepository failedMailsRepository;
    private PostOffice postOffice;

    private Message aMessage;
    private Faker faker = new Faker();
    private String anEmail;

    @Before
    public void setUp() {
        PostMan failingPostMan = message -> {
            throw new UnableToSendMessage(new MessagingException("Upsis"));
        };
        postOffice = new FailProofPostOffice(
                failingPostMan,
                failedMailsRepository
        );
        anEmail = faker.internet().emailAddress();
        aMessage = new Message(anEmail,
                faker.lorem().sentence(),
                faker.lorem().paragraph()
        );
    }

    @Test
    public void When_A_Mail_Fails_It_Should_Be_Stored_For_Future_resend() {
        postOffice.sendMessage(aMessage);

        assertThat(postOffice.getFailedMessages(), hasSize(1));
        assertThat(postOffice.getFailedMessages(), contains(hasProperty("recipient", is(anEmail))));
    }
}